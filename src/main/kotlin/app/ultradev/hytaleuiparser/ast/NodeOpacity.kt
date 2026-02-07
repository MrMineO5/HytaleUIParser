package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone

class NodeOpacity(
    children: List<AstNode>,
    valid: Boolean = true
) : AstNode(children, valid), VariableValue {
    val startMarker by child<NodeToken>(0)
    val value by child<NodeConstant>(1)
    val endMarker by child<NodeToken>(2)

    override fun clone() = NodeOpacity(children.clone(), valid)
}
