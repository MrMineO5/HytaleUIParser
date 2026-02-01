package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeRefMember(
    val reference: NodeReference,
    val memberMarker: NodeToken,
    val member: NodeVariable
) : AstNode(), VariableReference {
    override val children: List<AstNode>
        get() = listOf(reference, memberMarker, member)

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        reference.setScope(scope)

        memberMarker.setScope(scope)
    }

    override val resolvedValue get() = member.resolvedValue

    override fun clone() = NodeRefMember(reference.clone(), memberMarker.clone(), member.clone())
}
