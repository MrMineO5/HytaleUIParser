package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone

class NodeIdentifiedType(
    children: List<AstNode>,
    valid: Boolean = true
) : NodeType(children, valid), VariableValue {
    val type by child<NodeIdentifier>(0)
    override val body by child<NodeBody>(1)

    override fun clone() = NodeIdentifiedType(children.clone(), valid)
}
