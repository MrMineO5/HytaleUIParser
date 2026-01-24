package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.token.Token

class TokenIterator(val iterator: Iterator<Token>) : Iterator<Token> by iterator {
    private var peeked = mutableListOf<Token>()

    override fun next(): Token {
        if (peeked.isNotEmpty()) return peeked.removeAt(0)
        return iterator.next()
    }

    fun peek(lookAhead: Int = 1): Token {
        while (peeked.size < lookAhead) peeked.add(iterator.next())
        return peeked[lookAhead - 1]
    }
}