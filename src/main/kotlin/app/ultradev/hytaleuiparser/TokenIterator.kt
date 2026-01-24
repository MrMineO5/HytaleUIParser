package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.token.Token

class TokenIterator(val iterator: Iterator<Token>, val skipComments: Boolean = false) : Iterator<Token> {
    private var peeked = mutableListOf<Token>()

    private fun internalNext(): Token {
        var next = iterator.next()
        while (next.type == Token.Type.COMMENT && skipComments) next = iterator.next()
        return next
    }

    override fun next(): Token {
        if (peeked.isNotEmpty()) return peeked.removeAt(0)
        return internalNext()
    }

    override fun hasNext(): Boolean {
        return iterator.hasNext()
    }

    fun peek(lookAhead: Int = 1): Token {
        while (peeked.size < lookAhead) peeked.add(internalNext())
        return peeked[lookAhead - 1]
    }
}