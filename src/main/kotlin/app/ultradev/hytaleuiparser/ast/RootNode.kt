package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.ValidatorError
import app.ultradev.hytaleuiparser.validation.Scope

data class RootNode(
    override val children: List<AstNode>
) : AstNode() {
    override fun validate(validationError: (String, AstNode) -> Unit) {
        children.forEach {
            if (it is NodeAssignReference || it is NodeAssignVariable || it is NodeElement) return@forEach
            validationError("Unexpected node in file root. Expected NodeAssignReference, NodeAssignVariable, or NodeElement.", it)
        }
    }

    val references: List<NodeAssignReference> get() = children.filterIsInstance<NodeAssignReference>()
    val variables: List<NodeAssignVariable> get() = children.filterIsInstance<NodeAssignVariable>()
    val elements: List<NodeElement> get() = children.filterIsInstance<NodeElement>()

    lateinit var path: String
    lateinit var variableValues: Map<String, AstNode>

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        children.forEach { it.setScope(scope) }
    }
}
