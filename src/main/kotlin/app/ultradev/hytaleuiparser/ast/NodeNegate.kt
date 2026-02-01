package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeNegate(
    val minus: NodeToken,
    val param: AstNode
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(minus, param)

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        param.setScope(scope)

        minus.setScope(scope)
    }

    override fun clone() = NodeNegate(minus.clone(), param.clone())
}
