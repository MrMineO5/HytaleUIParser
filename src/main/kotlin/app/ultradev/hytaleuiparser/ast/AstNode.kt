package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.token.Token
import app.ultradev.hytaleuiparser.validation.Scope

sealed class AstNode {
    abstract val children: List<AstNode>
    open val tokens: List<Token>
        get() = children.flatMap { it.tokens }

    val text: String get() = tokens.joinToString("") { it.text }

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
}
