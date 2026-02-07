package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone
import app.ultradev.hytaleuiparser.validation.Scope

class NodeIdentifier(
    children: List<AstNode>,
    valid: Boolean = true
) : AstNode(children, valid) {
    val token by child<NodeToken>(0)

    val identifier get() = token.text

    override fun clone() = NodeIdentifier(children.clone(), valid)
}