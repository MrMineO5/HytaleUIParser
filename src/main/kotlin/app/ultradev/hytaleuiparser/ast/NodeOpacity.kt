package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeOpacity(
    val start: NodeToken,
    val end: NodeToken,
    val value: NodeConstant
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(start, value, end)

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        children.forEach { it.setScope(scope) }
    }
}
