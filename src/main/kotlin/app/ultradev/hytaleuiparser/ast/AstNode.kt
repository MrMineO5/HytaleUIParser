package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.ast.visitor.AstVisitor
import app.ultradev.hytaleuiparser.token.Token
import app.ultradev.hytaleuiparser.validation.Scope

sealed class AstNode {
    abstract val children: List<AstNode>
    open val tokens: List<Token>
        get() = children.flatMap { it.tokens }

    val text: String get() = tokens.joinToString("") { it.text }
    open val textRange: Pair<Int, Int> get() = children.first().textRange.first to children.last().textRange.second

    lateinit var parent: AstNode
    lateinit var file: RootNode
    var resolvedScope: Scope? = null
        private set

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


    internal fun validate0(validationError: (String, AstNode) -> Unit) {
        validate(validationError)
        children.forEach { it.validate0(validationError) }
    }
    protected open fun validate(validationError: (String, AstNode) -> Unit) {}


    internal fun startValidation0()  {
        startValidation()
        children.forEach { it.startValidation0() }
    }
    protected open fun startValidation() {}


    open fun computePath(): String = parent.computePath()


    fun getAstPath(): String {
        if (parent == this) return ""
        return parent.getAstPath() + "." + javaClass.simpleName
    }

    /**
     * Create a deep copy of this subtree
     *
     * Does **NOT** clone validation data
     */
    abstract fun clone(): AstNode
}
