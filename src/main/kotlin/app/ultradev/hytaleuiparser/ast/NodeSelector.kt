package app.ultradev.hytaleuiparser.ast

data class NodeSelector(
    val selector: NodeToken
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(selector)
}
