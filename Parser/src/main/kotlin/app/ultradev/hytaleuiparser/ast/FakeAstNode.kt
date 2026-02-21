package app.ultradev.hytaleuiparser.ast

object FakeAstNode : AstNode(emptyList(), false) {
    override fun clone(): AstNode = this
}