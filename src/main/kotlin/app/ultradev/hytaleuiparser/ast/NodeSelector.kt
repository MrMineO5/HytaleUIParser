package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone

class NodeSelector(
    children: List<AstNode>,
    valid: Boolean = true
) : AstNode(children, valid) {
    val selector by child<NodeToken>(0)

    val identifier get() = selector?.text

    override fun clone() = NodeSelector(children.clone(), valid)
}
