package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.token.Token
import app.ultradev.hytaleuiparser.token.TokenSymbols
import java.io.BufferedReader
import java.io.Reader

class Tokenizer(
    reader: Reader
) {
    val reader = BufferedReader(reader)

    var line = 0
    var column = 0

    fun nextToken(): Token? {
        val ch = readNWSP() ?: return null

        val startLine = line
        val startColumn = column - 1

        // Basic single meaning symbols have static tokens
        if (ch in TokenSymbols.TOKEN_MAP.keys) {
            return Token(TokenSymbols.TOKEN_MAP[ch]!!, ch.toString(), startLine, startColumn)
        }

        if (ch == '.') {
            if (peek() != '.') return Token(Token.Type.MEMBER, ch.toString(), startLine, startColumn)
            read()
            if (peek() != '.') error("Found two dots, maybe a typo in spread operator?")
            return Token(Token.Type.SPREAD, "...", startLine, startColumn)
        }

        if (ch == '"') {
            val sb = StringBuilder()
            var peek = peek()
            while (peek != '"' && peek != null) {
                sb.append(read())
                peek = peek()
            }
            read() // Skips quote, does nothing if EOF
            return Token(Token.Type.STRING, sb.toString(), startLine, startColumn)
        }

        // Parse identifier
        val sb = StringBuilder()
        sb.append(ch)

        while (peek()?.isLetterOrDigit() == true) sb.append(read())
        return Token(Token.Type.IDENTIFIER, sb.toString(), startLine, startColumn)
    }

    private fun readNWSP(): Char? {
        var ch = read()
        while (ch?.isWhitespace() == true) ch = read()
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