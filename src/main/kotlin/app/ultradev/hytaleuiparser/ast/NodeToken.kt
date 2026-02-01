package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.token.Token

data class NodeToken(
    val token: Token
) : AstNode() {
    override val children: List<AstNode> get() = emptyList()
    override val tokens: List<Token>
        get() = listOf(token)
    override val textRange: Pair<Int, Int>
        get() = token.startOffset to (token.startOffset + token.text.length)

    override fun clone(): NodeToken = NodeToken(token)
}
