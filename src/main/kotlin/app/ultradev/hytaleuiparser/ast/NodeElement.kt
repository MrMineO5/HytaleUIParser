package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.ElementType
import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.types.TypeType

// TODO: We could make NodeElementNormal an open class instead of having an abstract one, and override the index for the body
abstract class NodeElement(
    children: List<AstNode>,
    valid: Boolean,
) : AstNode(children, valid), VariableValue {
    abstract val type: AstNode
    abstract val body: NodeBody

    override fun validate(validationError: (String, AstNode) -> Unit) {
        body.elements.zipWithNext().forEach { (prev, curr) ->
            if (curr is NodeAssignVariable) {
                if (prev !is NodeAssignVariable) validationError("Variables must come first", curr)
                return@forEach
            }
            if (curr is NodeField) {
                if (prev is NodeElement || prev is NodeSelectorElement) validationError(
                    "Fields must come before elements",
                    curr
                )
                return@forEach
            }
            if (curr is NodeElement || curr is NodeSelectorElement) return@forEach

            validationError(
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

    override val resolvedTypes: Set<TypeType>
        get() = error("Elements can be variable values, but do not allow resolution as a TypeType, use resolvedType instead")

    override fun propagateScope(scope: Scope) {
        type.setScope(scope)
    }

    override fun computePath(): String = super.computePath() + "/${type.text}"

    abstract override fun clone(): NodeElement
}
