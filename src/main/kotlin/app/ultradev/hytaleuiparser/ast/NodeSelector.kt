package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeSelector(
    val selector: NodeToken
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(selector)

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        selector.setScope(scope)
    }
}
