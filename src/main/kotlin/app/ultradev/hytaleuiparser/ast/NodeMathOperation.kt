package app.ultradev.hytaleuiparser.ast

data class NodeMathOperation(
    val param1: AstNode,
    val operator: NodeToken,
    val param2: AstNode
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(param1, operator, param2)
}
