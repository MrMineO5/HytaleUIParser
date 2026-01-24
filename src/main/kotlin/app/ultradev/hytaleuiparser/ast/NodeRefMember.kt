package app.ultradev.hytaleuiparser.ast

data class NodeRefMember(
    val reference: NodeReference,
    val memberMarker: NodeToken,
    val member: NodeVariable
) : AstNode(), VariableReference {
    override val children: List<AstNode>
        get() = listOf(reference, memberMarker, member)
}
