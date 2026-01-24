package app.ultradev.hytaleuiparser.ast

data class NodeOpacity(
    val start: NodeToken,
    val end: NodeToken,
    val value: NodeConstant
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(start, value, end)
}
