package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeBody(
    val startToken: NodeToken,
    val endToken: NodeToken,
    val elements: List<AstNode>
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(startToken) + elements + listOf(endToken)

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        elements.forEach { it.setScope(scope) }
    }
}
