package app.ultradev.hytaleuiparser.ast

data class NodeReference(
    val refMarker: NodeToken,
    val identifier: NodeIdentifier
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(refMarker, identifier)
}
