package app.ultradev.hytaleuiparser.ast

data class NodeArray(
    val startToken: NodeToken,
    val elements: List<AstNode>,
    val endToken: NodeToken
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(startToken) + elements + listOf(endToken)
}
