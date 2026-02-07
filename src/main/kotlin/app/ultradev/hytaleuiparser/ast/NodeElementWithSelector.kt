package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone

class NodeElementWithSelector(
    children: List<AstNode>,
    valid: Boolean = true
) : NodeElement(children, valid) {
    override val type by child<AstNode>(0)
    val selector by child<NodeSelector>(1)
    override val body by child<NodeBody>(2)

    override fun clone() = NodeElementWithSelector(children.clone(), valid)
}
