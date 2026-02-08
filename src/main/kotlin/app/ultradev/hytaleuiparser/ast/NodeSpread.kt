package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone

class NodeSpread(
    children: List<AstNode>,
    valid: Boolean = true
) : AstNode(children, valid) {
    val spreadMarker by child<NodeToken>(0)
    val variable by child<AstNode>(1)
    val endStatement by optionalChild<NodeToken>(2)

    override fun validate(validationError: (String, AstNode) -> Unit) {
        if (variable !is VariableReference) validationError(
            "Expected variable reference after spread operator",
            findClosestChild(1)
        )
    }

    val variableAsReference: VariableReference = variable as VariableReference

    override fun clone() = NodeSpread(children.clone(), valid)
}
