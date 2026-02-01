package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.types.TypeType

data class NodeType(
    val type: NodeIdentifier?,
    val body: NodeBody
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOfNotNull(type) + listOf(body)

    override fun validate(validationError: (String, AstNode) -> Unit) {
        body.elements.forEach {
            if (it is NodeField || it is NodeSpread) return@forEach
            validationError(
                "Unexpected node in element body. Expected NodeField or NodeSpread.",
                it
            )
        }
    }

    val spreads: List<NodeSpread> get() = body.elements.filterIsInstance<NodeSpread>()
    val fields: List<NodeField> get() = body.elements.filterIsInstance<NodeField>()

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        body.setScope(scope)
        type?.setScope(scope)
    }

    override val resolvedTypes = mutableSetOf<TypeType>()

    override fun startValidation() {
        resolvedTypes.clear()
    }

    fun resolveValue(deep: Boolean = false): Map<String, VariableValue> {
        val output = mutableMapOf<String, VariableValue>()
        spreads.forEach {
            val res = it.variableAsReference.resolvedValue ?: return@forEach
            if (res !is NodeType) error("Spread resolved to non-type: $res")
            output.putAll(res.resolveValue(deep))
        }
        fields.forEach {
            var value = it.valueAsVariableValue
            if (value is VariableReference && deep) value = value.deepResolve() ?: return@forEach
            output[it.identifier.identifier] = value
        }
        return output
    }

    override fun clone() = NodeType(type?.clone(), body.clone())
}
