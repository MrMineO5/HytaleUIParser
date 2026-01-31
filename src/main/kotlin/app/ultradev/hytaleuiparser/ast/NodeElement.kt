package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.ValidatorException
import app.ultradev.hytaleuiparser.validation.ElementType
import app.ultradev.hytaleuiparser.validation.Scope

data class NodeElement(
    val type: AstNode,
    val body: NodeBody,
    val selector: NodeSelector? = null
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(type) + listOfNotNull(selector) + listOf(body)

    fun validate() {
        body.elements.zipWithNext().forEach { (prev, curr) ->
            if (curr is NodeAssignVariable) {
                if (prev !is NodeAssignVariable) throw ValidatorException("Variables must come first", curr)
                return@forEach
            }
            if (curr is NodeField) {
                if (prev is NodeElement || prev is NodeSelectorElement) throw ValidatorException("Fields must come before elements", curr)
                return@forEach
            }
            if (curr is NodeElement || curr is NodeSelectorElement) return@forEach

            throw ValidatorException(
                "Unexpected node in element body. Expected NodeAssignVariable, NodeField, NodeElement, or NodeSelectorElement.",
                curr
            )
        }
    }

    val localVariables: List<NodeAssignVariable> get() = body.elements.filterIsInstance<NodeAssignVariable>()
    val properties: List<NodeField> get() = body.elements.filterIsInstance<NodeField>()
    val childElements: List<NodeElement> get() = body.elements.filterIsInstance<NodeElement>()
    val selectorElements: List<NodeSelectorElement> get() = body.elements.filterIsInstance<NodeSelectorElement>()

    lateinit var resolvedType: ElementType

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        type.setScope(scope)
    }
}
