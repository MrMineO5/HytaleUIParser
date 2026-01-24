package app.ultradev.hytaleuiparser.ast

data class NodeNegate(
    val minus: NodeToken,
    val param: AstNode
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(minus, param)
}
