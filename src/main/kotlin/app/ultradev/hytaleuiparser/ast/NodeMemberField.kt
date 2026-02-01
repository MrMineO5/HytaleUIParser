package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeMemberField(
    val owner: AstNode,
    val memberMarker: NodeToken,
    val member: NodeIdentifier
) : AstNode(), VariableReference {
    override val children: List<AstNode>
        get() = listOf(owner, memberMarker, member)

    override fun validate(validationError: (String, AstNode) -> Unit) {
        if (owner !is VariableReference) validationError("Expected variable reference before member field", owner)
    }

    val ownerAsVariableReference: VariableReference = owner as VariableReference

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        owner.setScope(scope)
    }

    override val resolvedValue: VariableValue?
        get() {
            val owner = ownerAsVariableReference.resolvedValue
            if (owner !is NodeType) error("Member marker used on non-type")
            return owner.resolveValue()[member.identifier]
        }

    override fun clone() = NodeMemberField(owner.clone(), memberMarker.clone(), member.clone())
}
