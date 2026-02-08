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
    var offset = 0

    override fun hasNext(): Boolean {
        return peek() != null
    }

    override fun next(): Token {
        val ch = read() ?: error("next was called on an empty tokenizer")
        if (ch.customIsWhitespace()) {
            return Token(Token.Type.WHITESPACE, ch.toString(), line, column, offset - 1)
        }

        val startLine = line
        val startColumn = column - 1
        val startOffset = offset - 1


        // Dots can be either a member or a spread operator
        if (ch == '.') {
            if (peek() != '.') return Token(Token.Type.MEMBER_MARKER, ch.toString(), startLine, startColumn, startOffset)
            read()
            if (peek() != '.') return Token(Token.Type.UNKNOWN, "..", startLine, startColumn, startOffset)
            read()
            return Token(Token.Type.SPREAD, "...", startLine, startColumn, startOffset)
        }

        // Quotes strings
        if (ch == '"') {
            val sb = StringBuilder("\"")
            var peek = peek()
            var escaped = false
            while ((escaped || peek != '"') && peek != null) {
                escaped = if (peek == '\\') true else false
                sb.append(read())
                peek = peek()
            }
            val ch = read()  // Skips quote, does nothing if EOF
                ?: return Token(Token.Type.UNKNOWN, sb.toString(), startLine, startColumn, startOffset)
            sb.append(ch)
            return Token(Token.Type.STRING, sb.toString(), startLine, startColumn, startOffset)
        }

        // Comments
        if (ch == '/') {
            if (peek() == '/') {
                val sb = StringBuilder()
                sb.append(ch)
                sb.append(read())
                while (peek() != '\n') sb.append(read())
                return Token(Token.Type.COMMENT, sb.toString(), startLine, startColumn, startOffset)
            }
            if (peek() == '*') {
                val sb = StringBuilder()
                sb.append(ch)
                sb.append(read())
                while (true) {
                    while ((peek() ?: break) != '*') sb.append(read())
                    sb.append(read())

                    if ((peek() ?: break) == '/') {
                        sb.append(read())
                        break
                    }
                }
                return Token(Token.Type.COMMENT, sb.toString(), startLine, startColumn, startOffset)
            }
        }

        // Basic single meaning symbols have static tokens
        if (ch in TokenSymbols.TOKEN_MAP.keys) {
            return Token(TokenSymbols.TOKEN_MAP[ch]!!, ch.toString(), startLine, startColumn, startOffset)
        }

        val sb = StringBuilder()
        sb.append(ch)

        if (ch.isDigit()) {
            // Identifiers cannot start with a digit, this must be a number
            while (peek()?.isDigit() == true || peek() == '.') sb.append(read())
            return Token(Token.Type.NUMBER, sb.toString(), startLine, startColumn, startOffset)
        } else if (ch.isLetter()) {
            // Identifier
            while (peek()?.isLetterOrDigit() == true) sb.append(read())
            return Token(Token.Type.IDENTIFIER, sb.toString(), startLine, startColumn, startOffset)
        } else if (ch == '#' || ch == '@' || ch == '$') {
            // Selector or color
            while (peek()?.isLetterOrDigit() == true) sb.append(read())

            val type = when (ch) {
                '#' -> Token.Type.SELECTOR
                '@' -> Token.Type.VARIABLE
                '$' -> Token.Type.REFERENCE
                else -> error("Unknown token type")
            }

            return Token(type, sb.toString(), startLine, startColumn, startOffset)
        } else {
            return Token(Token.Type.UNKNOWN, sb.toString(), startLine, startColumn, startOffset)
        }
    }

    fun Char.customIsWhitespace(): Boolean = this.isWhitespace() || this == '﻿'

    private fun skipLine() {
        while (peek() != '\n') read()
        read()
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
        offset++
        return ch.toChar()
    }

    private fun peek(): Char? {
        reader.mark(1)
        val next = reader.read()
        reader.reset()
        return if (next == -1) null else next.toChar()
    }
}