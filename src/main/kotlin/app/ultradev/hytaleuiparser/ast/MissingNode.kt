package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.token.Token

data class MissingNode(
    val line: Int,
    val column: Int,
    val offset: Int,
) : AstNode(emptyList(), false) {
    override val startOffset: Int
        get() = offset
    override val endOffset: Int
        get() = offset

    override fun clone(): AstNode = copy()

    companion object {
        fun before(token: Token) = MissingNode(token.startLine, token.startColumn, token.startOffset)
        fun after(token: Token) = MissingNode(
            token.startLine + token.text.count { it == '\n' },
            token.startColumn + token.text.substringAfterLast("\n").length,
            token.startOffset + token.text.length
        )
    }
}