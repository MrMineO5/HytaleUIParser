package app.ultradev.hytaleuiparser.ast

data class NodeVariable(
    val variableMarker: NodeToken,
    val identifier: NodeIdentifier
) : AstNode(), VariableReference {
    override val children: List<AstNode>
        get() = listOf(variableMarker, identifier)
}
