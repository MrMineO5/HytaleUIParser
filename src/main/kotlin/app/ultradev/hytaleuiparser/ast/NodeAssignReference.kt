package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone
import app.ultradev.hytaleuiparser.validation.Scope
import app.ultradev.hytaleuiparser.validation.resolveNeighbour

class NodeAssignReference(
    children: List<AstNode>,
    valid: Boolean = true,
) : AstNode(children, valid) {
    val variable by child<NodeReference>(0)
    val assignMarker by child<NodeToken>(1)
    val filePath by child<NodeConstant>(2)
    val endStatement by child<NodeToken>(3)

    val resolvedFilePath: String get() = file.path.resolveNeighbour(filePath.valueText)


    override fun computePath(): String = super.computePath() + variable.identifier

    override fun clone() = NodeAssignReference(children.clone(), valid)
}
