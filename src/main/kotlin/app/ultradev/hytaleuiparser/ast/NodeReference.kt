package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.ValidatorError

data class NodeReference(
    val reference: NodeToken
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(reference)

    val identifier get() = reference.text

    override fun validate(validationError: (String, AstNode) -> Unit) {
        if (identifier == "$") validationError("Reference name cannot be empty", this)
    }

    val resolvedAssignment: NodeAssignReference? get() = resolvedScope.lookupReferenceAssignment(identifier)
}
