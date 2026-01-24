package app.ultradev.hytaleuiparser.ast

data class NodeColor(
    val colorMarker: NodeToken,
    val value: NodeConstant,
    val opacity: NodeOpacity? = null
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(colorMarker, value) + listOfNotNull(opacity)
}
