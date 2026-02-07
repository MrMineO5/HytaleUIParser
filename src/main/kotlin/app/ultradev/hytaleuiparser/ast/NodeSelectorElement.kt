package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone
import app.ultradev.hytaleuiparser.validation.Scope

class NodeSelectorElement(
    children: List<AstNode>,
    valid: Boolean = true
) : AstNode(children, valid) {
    val selector by child<NodeSelector>(0)
    val body by child<NodeBody>(1)

    override fun validate(validationError: (String, AstNode) -> Unit) {
        body.elements.forEach {
            if (it is NodeAssignVariable || it is NodeField || it is NodeElement) return@forEach
            validationError(
                "Unexpected node in element body. Expected NodeAssignVariable, NodeField, or NodeElement.",
                it
            )
        }
    }

    override fun propagateScope(scope: Scope) {
        selector.setScope(scope)
    }

    val localVariables: List<NodeAssignVariable> = body.elements.filterIsInstance<NodeAssignVariable>()
    val properties: List<NodeField> = body.elements.filterIsInstance<NodeField>()
    val childElements: List<NodeElement> = body.elements.filterIsInstance<NodeElement>()

    override fun clone() = NodeSelectorElement(children.clone(), valid)
}
