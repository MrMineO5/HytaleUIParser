package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.validation.Scope

data class NodeReference(
    val reference: NodeToken
) : AstNode() {
    override val children: List<AstNode>
        get() = listOf(reference)

    val identifier get() = reference.text

    override fun validate(validationError: (String, AstNode) -> Unit) {
        if (identifier == "$") validationError("Reference name cannot be empty", this)
    }

    override fun setScope(scope: Scope) {
        super.setScope(scope)
        reference.setScope(scope)
    }

    val resolvedAssignment: NodeAssignReference? get() = file.referenceMap[identifier]

    override fun clone() = NodeReference(reference.clone())
}
