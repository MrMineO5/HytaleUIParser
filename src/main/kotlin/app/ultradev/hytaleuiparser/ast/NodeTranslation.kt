package app.ultradev.hytaleuiparser.ast

data class NodeTranslation(
    val translationMarker: NodeToken,
    val value: NodeConstant
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(translationMarker, value)
}
