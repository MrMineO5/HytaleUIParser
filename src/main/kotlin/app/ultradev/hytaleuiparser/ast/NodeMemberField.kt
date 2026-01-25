package app.ultradev.hytaleuiparser.ast

data class NodeMemberField(
    val parent: AstNode,
    val memberMarker: NodeToken,
    val member: NodeIdentifier
) : AstNode(), VariableValue {
    override val children: List<AstNode>
        get() = listOf(parent, memberMarker, member)
}
