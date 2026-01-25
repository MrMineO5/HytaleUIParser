package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeRefMember(
    val reference: NodeReference,
    val memberMarker: NodeToken,
    val member: NodeVariable
) : AstNode(), VariableReference {
    override val children: List<AstNode>
        get() = listOf(reference, memberMarker, member)

    override lateinit var resolvedScope: Scope

    override fun _initResolvedScope(scope: Scope) {
        resolvedScope = scope
    }
}
