package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeMathOperation(
    val param1: AstNode,
    val operator: NodeToken,
    val param2: AstNode
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(param1, operator, param2)

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        param1.setScope(scope)
        param2.setScope(scope)

        operator.setScope(scope)
    }

    override fun clone() = NodeMathOperation(param1.clone(), operator.clone(), param2.clone())
}
