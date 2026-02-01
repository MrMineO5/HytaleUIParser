package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeSelectorElement(
    val selector: NodeSelector,
    val body: NodeBody,
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(selector, body)

    override fun validate(validationError: (String, AstNode) -> Unit) {
        body.elements.forEach {
            if (it is NodeAssignVariable || it is NodeField || it is NodeElement) return@forEach
            validationError(
                "Unexpected node in element body. Expected NodeAssignVariable, NodeField, or NodeElement.",
                it
            )
        }
    }

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        selector.setScope(scope)
    }

    val localVariables: List<NodeAssignVariable> = body.elements.filterIsInstance<NodeAssignVariable>()
    val properties: List<NodeField> = body.elements.filterIsInstance<NodeField>()
    val childElements: List<NodeElement> = body.elements.filterIsInstance<NodeElement>()

    override fun clone() = NodeSelectorElement(selector.clone(), body.clone())
}
