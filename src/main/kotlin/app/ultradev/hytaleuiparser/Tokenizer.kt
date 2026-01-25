package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.token.Token
import app.ultradev.hytaleuiparser.token.TokenSymbols
import java.io.BufferedReader
import java.io.Reader

class Tokenizer(
    reader: Reader
) : Iterator<Token> {
    val reader = BufferedReader(reader)

    var line = 0
    var column = 0

    override fun hasNext(): Boolean {
        skipWhitespace()
        return peek() != null
    }

    override fun next(): Token {
        val ch = readNWSP()!!

        val startLine = line
        val startColumn = column - 1


        // Dots can be either a member or a spread operator
        if (ch == '.') {
            if (peek() != '.') return Token(Token.Type.MEMBER_MARKER, ch.toString(), startLine, startColumn)
            read()
            if (peek() != '.') error("Found two dots, maybe a typo in spread operator?")
            read()
            return Token(Token.Type.SPREAD, "...", startLine, startColumn)
        }

        // Quotes strings
        if (ch == '"') {
            val sb = StringBuilder("\"")
            var peek = peek()
            while (peek != '"' && peek != null) {
                sb.append(read())
                peek = peek()
            }
            val ch = read() ?: error("Unterminated string") // Skips quote, does nothing if EOF
            sb.append(ch)
            return Token(Token.Type.STRING, sb.toString(), startLine, startColumn)
        }

        // Comments
        if (ch == '/') {
            if (peek() == '/') {
                val sb = StringBuilder()
                sb.append(ch)
                sb.append(read())
                while (peek() != '\n') sb.append(read())
                read()
                return Token(Token.Type.COMMENT, sb.toString(), startLine, startColumn)
            }
            if (peek() == '*') {
                val sb = StringBuilder()
                sb.append(ch)
                sb.append(read())
                while (true) {
                    while (peek() != '*') sb.append(read())
                    sb.append(read())
                    if (peek() == '/') {
                        sb.append(read())
                        break
                    }
                }
                return Token(Token.Type.COMMENT, sb.toString(), startLine, startColumn)
            }
        }

        // Basic single meaning symbols have static tokens
        if (ch in TokenSymbols.TOKEN_MAP.keys) {
            return Token(TokenSymbols.TOKEN_MAP[ch]!!, ch.toString(), startLine, startColumn)
        }

        val sb = StringBuilder()
        sb.append(ch)

        if (ch.isDigit()) {
            // Identifiers cannot start with a digit, this must be a number
            while (peek()?.isDigit() == true || peek() == '.') sb.append(read())
            return Token(Token.Type.NUMBER, sb.toString(), startLine, startColumn)
        } else if (ch.isLetter()) {
            // Identifier
            while (peek()?.isLetterOrDigit() == true) sb.append(read())
            return Token(Token.Type.IDENTIFIER, sb.toString(), startLine, startColumn)
        } else if (ch == '#') {
            // Selector or color
            while (peek()?.isLetterOrDigit() == true) sb.append(read())
            return Token(Token.Type.SELECTOR, sb.toString(), startLine, startColumn)
        } else {
            error("Unexpected character: $ch")
        }
    }

    fun Char.customIsWhitespace(): Boolean = this.isWhitespace() || this == '﻿'

    private fun skipWhitespace() {
        while (peek()?.customIsWhitespace() == true) read()
    }

    private fun skipLine() {
        while (peek() != '\n') read()
        read()
    }

    private fun readNWSP(): Char? {
        var ch = read()
        while (ch?.customIsWhitespace() == true) ch = read()
        return ch
    }

    private fun read(): Char? {
        val ch = reader.read()
        if (ch == -1) return null
        val char = ch.toChar()
        if (char == '\n') {
            line++
            column = 0
        } else {
            column++
        }
        return ch.toChar()
    }

    private fun peek(): Char? {
        reader.mark(1)
        val next = reader.read()
        reader.reset()
        return if (next == -1) null else next.toChar()
    }
}