package app.ultradev.hytaleuiparser.ast

data class NodeSelector(
    val selectorMarker: NodeToken,
    val identifier: NodeIdentifier
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(selectorMarker, identifier)
}
