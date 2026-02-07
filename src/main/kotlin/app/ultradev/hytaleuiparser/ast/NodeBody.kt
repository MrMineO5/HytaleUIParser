package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone

class NodeBody(
    children: List<AstNode>,
    valid: Boolean = true,
) : AstNode(children, valid) {
    val startToken by child<NodeToken>(0)
    val elements by children<AstNode>(1, -2)
    val endToken by child<NodeToken>(-1)

    override fun clone() = NodeBody(children.clone(), valid)
}
