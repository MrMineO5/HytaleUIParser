package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.token.Token

/**
 * Similar to [ParserException], but for errors that are recoverable
 */
data class ParserError(
    val message: String,
    val token: Token,
)
