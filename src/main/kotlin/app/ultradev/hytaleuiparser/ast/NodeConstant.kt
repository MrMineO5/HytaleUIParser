package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.token.Token
import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.types.TypeType
import java.util.EnumSet
import kotlin.math.floor

data class NodeConstant(
    override val children: List<NodeToken>,
    val valueText: String
) : AstNode(), VariableValue {
    constructor(token: NodeToken, valueText: String) : this(
        listOf(token),
        valueText
    )
    constructor(token: Token) : this(NodeToken(token), token.text)

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        children.forEach { it.setScope(scope) }
    }

    companion object {
        fun quoted(valueText: String) = NodeConstant(NodeToken(Token(Token.Type.STRING, "\"$valueText\"")), valueText)
    }

    fun getAllowedTypes(): Set<TypeType> {
        if (children.first().text.startsWith("\"")) return setOf(TypeType.String)
        if (valueText == "true" || valueText == "false") return setOf(TypeType.Boolean)
        val parsed = valueText.toDoubleOrNull() ?: return TypeType.entries.filter { it.enum.contains(valueText) }.toSet()
        if (floor(parsed) == parsed) return setOf(
            TypeType.Integer,
            TypeType.Float,
            TypeType.Double
        )

        return setOf(TypeType.Float, TypeType.Double)
    }

    override val resolvedTypes: Set<TypeType> get() = getAllowedTypes()
}
