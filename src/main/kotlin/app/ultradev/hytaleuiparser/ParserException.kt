package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.token.Token

/**
 * An exception thrown when the parser encounters an unrecoverable error while parsing
 */
class ParserException(
    message: String,
    val token: Token
) : Exception(message) {

    override fun toString(): String {
        return "ParserException(message='$message', token=$token)"
    }
}
