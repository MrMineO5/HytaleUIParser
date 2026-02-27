package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone

class NodeArray(children: List<AstNode>) : AstNode(children), VariableValue {
    val startToken by child<NodeToken>(0)
    val entries by children({ it is VariableValue }, 1, -2)
    val endToken by child<NodeToken>(-1)

    override fun clone() = NodeArray(children.clone())
}
