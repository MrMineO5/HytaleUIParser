package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeMemberField(
    val owner: AstNode,
    val memberMarker: NodeToken,
    val member: NodeIdentifier
) : AstNode(), VariableReference {
    override val children: List<AstNode>
        get() = listOf(owner, memberMarker, member)

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        owner.setScope(scope)
    }
}
