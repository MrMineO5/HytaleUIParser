package app.ultradev.hytaleuiparser.ast

data class NodeElement(
    val type: AstNode,
    val body: NodeBody,
    val selector: NodeSelector? = null
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(type) + listOfNotNull(selector) + listOf(body)

    init {
        // TODO: Maybe write this in a cleaner way?
        body.elements.zipWithNext().forEach { (prev, curr) ->
            if (curr is NodeAssignVariable) {
                if (prev !is NodeAssignVariable) error("Variables must come first")
                return@forEach
            }
            if (curr is NodeField) {
                if (prev is NodeElement || prev is NodeSelectorElement) error("Fields must come before elements")
                return@forEach
            }
            if (curr is NodeElement || curr is NodeSelectorElement) return@forEach

            error(
                "Unexpected node in element body: $curr. Expected NodeField, NodeElement, or NodeSelectorElement."
            )
        }
    }

    val localVariables: List<NodeAssignVariable> get() = body.elements.filterIsInstance<NodeAssignVariable>()
    val properties: List<NodeField> get() = body.elements.filterIsInstance<NodeField>()
    val childElements: List<NodeElement> get() = body.elements.filterIsInstance<NodeElement>()
    val selectorElements: List<NodeSelectorElement> get() = body.elements.filterIsInstance<NodeSelectorElement>()
}
