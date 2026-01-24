package app.ultradev.hytaleuiparser.ast

data class NodeElement(
    val type: AstNode,
    val body: NodeBody,
    val selector: NodeSelector? = null
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(type) + listOfNotNull(selector) + listOf(body)
}
