package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone

class NodeNegate(
    children: List<AstNode>,
    valid: Boolean = true
) : AstNode(children, valid), VariableValue {
    val minusMarker by child<NodeToken>(0)
    val param by child<AstNode>(1)

    override fun clone() = NodeNegate(children.clone(), valid)
}
