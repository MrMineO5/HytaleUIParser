package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone

class NodeMemberField(
    children: List<AstNode>,
    valid: Boolean = true
) : AstNode(children, valid), VariableReference {
    val owner by child<AstNode>(0)
    val memberMarker by child<NodeToken>(1)
    val member by child<NodeIdentifier>(2)

    override fun validate(validationError: (String, AstNode) -> Unit) {
        if (owner !is VariableReference) validationError("Expected variable reference before member field", owner)
    }

    val ownerAsVariableReference: VariableReference = owner as VariableReference

    override val resolvedValue: VariableValue?
        get() {
            val owner = ownerAsVariableReference.resolvedValue
            if (owner !is NodeType) error("Member marker used on non-type")
            return owner.resolveValue()[member.identifier]
        }

    override fun clone() = NodeMemberField(children.clone(), valid)
}
