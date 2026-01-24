package app.ultradev.hytaleuiparser.ast

data class NodeSelectorElement(
    val selector: NodeSelector,
    val body: NodeBody,
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(selector, body)
}
