package app.ultradev.hytaleuiparser.ast

data class NodeAssignReference(
    val variable: NodeReference,
    val assignMarker: NodeToken,
    val filePath: NodeConstant,
    val endStatement: NodeToken
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(variable, assignMarker, filePath, endStatement)
}
