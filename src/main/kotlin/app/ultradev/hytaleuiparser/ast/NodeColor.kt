package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

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
}
