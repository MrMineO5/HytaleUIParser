package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.ElementType
import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.types.TypeType

// TODO: We could make NodeElementNormal an open class instead of having an abstract one, and override the index for the body
open class NodeElement(
    children: List<AstNode>,
    valid: Boolean = true,
) : AstNode(children, valid), VariableValue {
    open val type by child<AstNode>(0)
    open val body by child<NodeBody>(1)

    override fun validate(validationError: (String, AstNode) -> Unit) {
        body?.elements?.zipWithNext()?.forEach { (prev, curr) ->
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

    val localVariables: List<NodeAssignVariable> get() = body?.elements?.filterIsInstance<NodeAssignVariable>() ?: emptyList()
    val properties: List<NodeField> get() = body?.elements?.filterIsInstance<NodeField>() ?: emptyList()
    val childElements: List<NodeElement> get() = body?.elements?.filterIsInstance<NodeElement>() ?: emptyList()
    val selectorElements: List<NodeSelectorElement> get() = body?.elements?.filterIsInstance<NodeSelectorElement>() ?: emptyList()

    override fun propagateScopeChildren(): List<AstNode> = listOfNotNull(type)

    lateinit var resolvedType: ElementType

    override val resolvedTypes: Set<TypeType>
        get() = error("Elements can be variable values, but do not allow resolution as a TypeType, use resolvedType instead")

    val resolvedVariableImplementation: NodeElement? get() = (type as? VariableReference)?.deepResolve() as? NodeElement

    fun resolveProperties(): Map<String, VariableValue> {
        val output = mutableMapOf<String, VariableValue>()
        resolvedVariableImplementation?.let { output.putAll(it.resolveProperties()) }
        properties.forEach {
            var value = it.valueAsVariableValue
            if (value is VariableReference) value = value.deepResolve() ?: return@forEach
            output[it.identifier!!.identifier] = value
        }
        return output
    }

    override fun computePath(): String = super.computePath() + "/${type?.text}"

    override fun clone(): NodeElement = NodeElement(children, valid)
}
