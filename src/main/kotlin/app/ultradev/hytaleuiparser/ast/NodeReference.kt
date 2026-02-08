package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone

class NodeReference(
    children: List<AstNode>,
    valid: Boolean = true
) : AstNode(children, valid) {
    val reference by child<NodeToken>(0)

    val identifier get() = reference!!.text

    override fun validate(validationError: (String, AstNode) -> Unit) {
        if (identifier == "$") validationError("Reference name cannot be empty", this)
    }

    val resolvedAssignment: NodeAssignReference? get() = file.referenceMap[identifier]

    override fun clone() = NodeReference(children.clone(), valid)
}
