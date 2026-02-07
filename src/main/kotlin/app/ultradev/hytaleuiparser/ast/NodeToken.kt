package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.token.Token

data class NodeToken(
    val token: Token
) : AstNode(emptyList(), true) {
    override val tokens: List<Token>
        get() = listOf(token)
    override val startOffset: Int
        get() = token.startOffset
    override val endOffset: Int
        get() = token.startOffset + token.text.length

    override fun clone(): NodeToken = NodeToken(token)
}
