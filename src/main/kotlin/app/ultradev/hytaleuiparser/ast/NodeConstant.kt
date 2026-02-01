package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.token.Token
import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.types.TypeType

data class NodeConstant(
    override val children: List<NodeToken>,
    val valueText: String
) : AstNode(), VariableValue {
    val isQuoted: Boolean get() = text != valueText

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
        valueText.toDoubleOrNull() ?: return TypeType.entries.filter { it.enum.contains(valueText) }.toSet()
        return setOf(
            TypeType.Int32,
            TypeType.Float
        )
    }

    override val resolvedTypes: Set<TypeType> get() = getAllowedTypes()

    override fun clone() = NodeConstant(children.map { it.clone() }, valueText)
}
