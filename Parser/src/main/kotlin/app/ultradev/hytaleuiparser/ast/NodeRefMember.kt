package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone
import app.ultradev.hytaleuiparser.validation.Scope

class NodeRefMember(
    children: List<AstNode>,
    valid: Boolean = true
) : AstNode(children, valid), VariableReference {
    val reference by child<NodeReference>(0)
    val memberMarker by child<NodeToken>(1)
    val member by child<NodeVariable>(2)

    override fun propagateScopeChildren(): List<AstNode> = listOfNotNull(reference, memberMarker)

    override val resolvedValue get() = member?.resolvedValue

    override fun clone() = NodeRefMember(children.clone(), valid)
}
