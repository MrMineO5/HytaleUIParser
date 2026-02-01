package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeBody(
    val startToken: NodeToken,
    val elements: List<AstNode>,
    val endToken: NodeToken,
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(startToken) + elements + listOf(endToken)

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        children.forEach { it.setScope(scope) }
    }

    override fun clone() = NodeBody(startToken.clone(), elements.map { it.clone() }, endToken.clone())
}
