package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.clone
import app.ultradev.hytaleuiparser.validation.resolveNeighbour

class NodeAssignReference(children: List<AstNode>) : AstNode(children) {
    val variable by child<NodeReference>(0)
    val assignMarker by child<NodeToken>(1)
    val filePath by child<NodeConstant>(2)
    val endStatement by child<NodeToken>(3)

    val resolvedFilePath: String? get() = filePath?.let { file.path.resolveNeighbour(it.valueText) }
    lateinit var resolvedFileRoot: RootNode


    override fun computePath(): String = super.computePath() + variable?.identifier

    override fun clone() = NodeAssignReference(children.clone())
}
