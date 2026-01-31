package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.token.Token

class TokenIterator(val iterator: Iterator<Token>, val skipComments: Boolean = false) : Iterator<Token> {
    private var peeked = mutableListOf<Token>()

    private fun isIgnored(token: Token): Boolean {
        return token.type == Token.Type.COMMENT && skipComments || token.type == Token.Type.WHITESPACE
    }

    private fun internalNext(): Token {
        var next = iterator.next()
        while (isIgnored(next))
            next = iterator.next()
        return next
    }

    override fun next(): Token {
        if (peeked.isNotEmpty()) return peeked.removeAt(0)
        return internalNext()
    }

    override fun hasNext() = hasNextMany()

    fun hasNextMany(count: Int = 1): Boolean {
        while (peeked.size < count) {
            if (!iterator.hasNext()) return false
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (isIgnored(next)) continue
                peeked.add(next)
            }
        }
        return true
    }

    fun peek(lookAhead: Int = 1): Token {
        while (peeked.size < lookAhead) peeked.add(internalNext())
        return peeked[lookAhead - 1]
    }
}