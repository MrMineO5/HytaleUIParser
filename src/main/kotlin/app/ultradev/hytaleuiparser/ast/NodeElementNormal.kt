package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone

class NodeElementNormal(
    children: List<AstNode>,
    valid: Boolean = true
) : NodeElement(children, valid) {
    override val type by child<AstNode>(0)
    override val body by child<NodeBody>(1)

    override fun clone() = NodeElementNormal(children.clone(), valid)
}
