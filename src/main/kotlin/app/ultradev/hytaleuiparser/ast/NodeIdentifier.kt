package app.ultradev.hytaleuiparser.ast

data class NodeIdentifier(
    val token: NodeToken,
    val identifier: String
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(token)
}