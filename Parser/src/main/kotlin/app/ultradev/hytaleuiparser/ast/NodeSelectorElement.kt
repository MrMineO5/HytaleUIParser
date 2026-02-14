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
        body?.elements?.forEach {
            if (it is NodeAssignVariable || it is NodeField || it is NodeElement) return@forEach
            validationError(
                "Unexpected node in element body. Expected NodeAssignVariable, NodeField, or NodeElement.",
                it
            )
        }
    }

    override fun propagateScopeChildren(): List<AstNode> = listOfNotNull(selector)

    val localVariables: List<NodeAssignVariable> get() = body?.elements?.filterIsInstance<NodeAssignVariable>() ?: emptyList()
    val properties: List<NodeField> get() = body?.elements?.filterIsInstance<NodeField>() ?: emptyList()
    val childElements: List<NodeElement> get() = body?.elements?.filterIsInstance<NodeElement>() ?: emptyList()


    fun resolveProperties(): Map<String, VariableValue> {
        val output = mutableMapOf<String, VariableValue>()
        properties.forEach {
            var value = it.valueAsVariableValue
            if (value is VariableReference) value = value.deepResolve() ?: return@forEach
            output[it.identifier!!.identifier] = value
        }
        return output
    }

    override fun clone() = NodeSelectorElement(children.clone(), valid)
}
