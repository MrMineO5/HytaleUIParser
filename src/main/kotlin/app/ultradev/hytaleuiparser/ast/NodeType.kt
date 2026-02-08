package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone
import app.ultradev.hytaleuiparser.validation.types.TypeType

open class NodeType(
    children: List<AstNode>,
    valid: Boolean = true
) : AstNode(children, valid), VariableValue {
    open val body by child<NodeBody>(0)

    override fun validate(validationError: (String, AstNode) -> Unit) {
        body?.elements?.forEach {
            if (it is NodeField || it is NodeSpread) return@forEach
            validationError(
                "Unexpected node in element body. Expected NodeField or NodeSpread.",
                it
            )
        }
    }

    val spreads: List<NodeSpread> get() = body?.elements?.filterIsInstance<NodeSpread>() ?: emptyList()
    val fields: List<NodeField> get() = body?.elements?.filterIsInstance<NodeField>() ?: emptyList()

    override val resolvedTypes = mutableSetOf<TypeType>()

    override fun startValidation() {
        resolvedTypes.clear()
    }

    fun resolveValue(): Map<String, VariableValue> {
        val output = mutableMapOf<String, VariableValue>()
        spreads.forEach {
            val res = it.variableAsReference.resolvedValue ?: return@forEach
            if (res !is NodeType) error("Spread resolved to non-type: $res")
            output.putAll(res.resolveValue())
        }
        fields.forEach {
            var value = it.valueAsVariableValue
            if (value is VariableReference) value = value.deepResolve() ?: return@forEach
            output[it.identifier!!.identifier!!] = value
        }
        return output
    }

    fun resolveFields(): Map<String, NodeField> {
        val output = mutableMapOf<String, NodeField>()
        spreads.forEach { spread ->
            val res = spread.variableAsReference.resolvedValue ?: return@forEach
            if (res !is NodeType) error("Spread resolved to non-type: $res")
            output.putAll(res.resolveFields())
        }
        fields.forEach { output[it.identifier!!.identifier!!] = it }
        return output
    }

    override fun clone() = NodeType(children.clone(), valid)
}
