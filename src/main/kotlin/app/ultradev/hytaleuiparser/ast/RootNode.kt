package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone
import app.ultradev.hytaleuiparser.validation.Scope

class RootNode(
    children: List<AstNode>
) : AstNode(children, true) {
    override fun validate(validationError: (String, AstNode) -> Unit) {
        children.forEach {
            if (it is NodeAssignReference || it is NodeAssignVariable || it is NodeElement) return@forEach
            validationError("Unexpected node in file root. Expected NodeAssignReference, NodeAssignVariable, or NodeElement.", it)
        }
    }

    val references: List<NodeAssignReference> get() = children.filterIsInstance<NodeAssignReference>()
    val variables: List<NodeAssignVariable> get() = children.filterIsInstance<NodeAssignVariable>()
    val elements: List<NodeElement> get() = children.filterIsInstance<NodeElement>()

    val referenceMap: Map<String, NodeAssignReference> by lazy {
        references.associateBy { it.variable.identifier }
    }

    lateinit var path: String

    override fun computePath(): String = path

    override fun clone() = RootNode(children.clone())
}
