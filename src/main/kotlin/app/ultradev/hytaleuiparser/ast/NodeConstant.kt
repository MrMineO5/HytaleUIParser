package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone
import app.ultradev.hytaleuiparser.validation.types.TypeType

class NodeConstant(
    children: List<AstNode>,
    valid: Boolean = true,
) : AstNode(children, valid), VariableValue {
    val valueText: String
        get() = text.let {
            if (it.startsWith("\"")) it.substring(1, it.length - 1) else it
        }

    val isQuoted: Boolean get() = text.startsWith("\"")

    fun getAllowedTypes(): Set<TypeType> {
        if (children.first().text.startsWith("\"")) return setOf(TypeType.String)
        if (valueText == "true" || valueText == "false") return setOf(TypeType.Boolean)
        valueText.toDoubleOrNull() ?: return TypeType.entries.filter { it.enum.contains(valueText) }.toSet()
        return setOf(
            TypeType.Int32,
            TypeType.Float
        )
    }

    override val resolvedTypes: Set<TypeType> get() = getAllowedTypes()

    override fun clone() = NodeConstant(children.clone(), valid)
}
