package app.ultradev.hytaleuiparser.ast

data class NodeElement(
    val type: AstNode,
    val body: NodeBody,
    val selector: NodeSelector? = null
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(type) + listOfNotNull(selector) + listOf(body)

    init {
        body.elements.forEach {
            if (it is NodeAssignVariable || it is NodeField || it is NodeElement || it is NodeSelectorElement) return@forEach
            error(
                "Unexpected node in element body: $it. Expected NodeField, NodeElement, or NodeSelectorElement."
            )
        }
    }

    val localVariables: List<NodeAssignVariable> = body.elements.filterIsInstance<NodeAssignVariable>()
    val properties: List<NodeField> = body.elements.filterIsInstance<NodeField>()
    val childElements: List<NodeElement> = body.elements.filterIsInstance<NodeElement>()
    val selectorElements: List<NodeSelectorElement> = body.elements.filterIsInstance<NodeSelectorElement>()
}
