package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.types.TypeType

data class NodeColor(
    val value: NodeConstant,
    val opacity: NodeOpacity? = null
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(value) + listOfNotNull(opacity)

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        value.setScope(scope)
        opacity?.setScope(scope)
    }

    override val resolvedTypes: Set<TypeType>
        get() = setOf(TypeType.Color)

    override fun clone() = NodeColor(value.clone(), opacity?.clone())
}
