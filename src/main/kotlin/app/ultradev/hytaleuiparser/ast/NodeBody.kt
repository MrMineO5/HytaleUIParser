package app.ultradev.hytaleuiparser.ast

data class NodeBody(
    val startToken: NodeToken,
    val endToken: NodeToken,
    val elements: List<AstNode>
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(startToken) + elements + listOf(endToken)
}
