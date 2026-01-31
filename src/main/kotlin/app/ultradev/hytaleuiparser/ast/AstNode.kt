package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.ast.visitor.AstVisitor
import app.ultradev.hytaleuiparser.token.Token
import app.ultradev.hytaleuiparser.validation.Scope

sealed class AstNode {
    abstract val children: List<AstNode>
    open val tokens: List<Token>
        get() = children.flatMap { it.tokens }

    val text: String get() = tokens.joinToString("") { it.text }
    val textRange: Pair<Int, Int> get() = tokens.let { tokens ->
        tokens.first().startOffset to (tokens.last().let { it.startOffset + it.text.length })
    }

    lateinit var parent: AstNode
    lateinit var file: RootNode
    lateinit var resolvedScope: Scope
        private set

    val isResolved get() = ::resolvedScope.isInitialized

    open fun setScope(scope: Scope) {
        resolvedScope = scope
    }

    fun initFile(file: RootNode) {
        this.file = file
        children.forEach { it.initFile(file) }
    }

    fun applyParent(parent: AstNode) {
        this.parent = parent
        children.forEach { it.applyParent(this) }
    }


    fun walk(visitor: AstVisitor) {
        visitor.visit(this)
        children.forEach { it.walk(visitor) }
    }
}
