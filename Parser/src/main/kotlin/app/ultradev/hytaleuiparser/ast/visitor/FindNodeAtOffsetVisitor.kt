package app.ultradev.hytaleuiparser.ast.visitor

import app.ultradev.hytaleuiparser.ast.AstNode

class FindNodeAtOffsetVisitor(val offset: Int) : AstVisitor {
    var bestMatch: AstNode? = null
    private var bestMatchSize = Int.MAX_VALUE

    override fun visit(node: AstNode) {
        val (start, end) = node.textRange
        if (offset in start..<end) {
            val size = end - start
            if (size <= bestMatchSize) {
                bestMatch = node
                bestMatchSize = size
            }
        }
    }
}

fun AstNode.findNodeAtOffset(offset: Int): AstNode? {
    val visitor = FindNodeAtOffsetVisitor(offset)
    walk(visitor)
    return visitor.bestMatch
}
