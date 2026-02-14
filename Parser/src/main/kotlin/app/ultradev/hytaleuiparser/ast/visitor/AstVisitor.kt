package app.ultradev.hytaleuiparser.ast.visitor

import app.ultradev.hytaleuiparser.ast.AstNode

interface AstVisitor {
    fun visit(node: AstNode)
}
