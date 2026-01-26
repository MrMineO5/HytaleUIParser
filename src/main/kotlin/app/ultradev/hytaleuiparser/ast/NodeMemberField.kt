package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeMemberField(
    val parent: AstNode,
    val memberMarker: NodeToken,
    val member: NodeIdentifier
) : AstNode(), VariableReference {
    override val children: List<AstNode>
        get() = listOf(parent, memberMarker, member)

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        parent.setScope(scope)
    }
}
