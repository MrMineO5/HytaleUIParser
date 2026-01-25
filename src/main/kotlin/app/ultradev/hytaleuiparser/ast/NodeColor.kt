package app.ultradev.hytaleuiparser.ast

data class NodeColor(
    val value: NodeConstant,
    val opacity: NodeOpacity? = null
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(value) + listOfNotNull(opacity)
}
