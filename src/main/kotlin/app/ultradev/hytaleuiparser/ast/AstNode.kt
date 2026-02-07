package app.ultradev.hytaleuiparser.ast

import app.ultradev.hytaleuiparser.ast.visitor.AstVisitor
import app.ultradev.hytaleuiparser.token.Token
import app.ultradev.hytaleuiparser.validation.Scope
import kotlin.reflect.KProperty

sealed class AstNode(
    val children: List<AstNode>,
    val valid: Boolean,
) {
    open val tokens: List<Token>
        get() = children.flatMap { it.tokens }

    val text: String get() = tokens.joinToString("") { it.text }

    open val startOffset: Int get() = children.first().startOffset
    open val endOffset: Int get() = children.last().endOffset
    val textRange: Pair<Int, Int> get() = startOffset to endOffset

    lateinit var parent: AstNode
    lateinit var file: RootNode
    var resolvedScope: Scope? = null
        private set

    /**
     * Set the scope of this node and propagate using its propagation rule.
     */
    internal fun setScope(scope: Scope) {
        resolvedScope = scope
        propagateScope(scope)
    }

    /**
     * Propagate the given scope to all children of this node.
     *
     * Can be overwritten in special cases to change this behavior (e.g. for nodes that create a scope)
     */
    internal open fun propagateScope(scope: Scope) {
        children.forEach { it.setScope(scope) }
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
    /**
     * Called at the beginning of validation.
     *
     * Currently this is only used to reset some validation state
     */
    protected open fun startValidation() {}


    open fun computePath(): String = parent.computePath()


    fun getAstPath(): String {
        if (parent == this) return ""
        return parent.getAstPath() + "." + javaClass.simpleName
    }

    override fun toString(): String {
        return "${javaClass.simpleName} {\n" +
                children.joinToString("\n") { it.toString() }.prependIndent() +
                "\n}"
    }

    /**
     * Create a deep copy of this subtree
     *
     * Does **NOT** clone validation data
     */
    abstract fun clone(): AstNode

    /**
     * Get the index'th child of this node. Negative indices count from the end of the list (i.e. -1 is the last child).
     */
    protected inline fun <reified T : AstNode> child(index: Int) = NodeDelegate<T>(index)
    protected inline fun <reified T : AstNode> optionalChild(index: Int) = NodeDelegate<T?>(index)
    protected class NodeDelegate<T : AstNode?>(val index: Int) {
        operator fun getValue(thisRef: AstNode, property: KProperty<*>): T {
            if (!thisRef.valid) throw IllegalStateException("Cannot access children of invalid node")

            val finalIndex = if (index < 0) thisRef.children.size + index else index
            return thisRef.children.getOrNull(finalIndex) as T
        }
    }

    /**
     * Get all children of this node that match the given type between startIndex and endIndex (both inclusive.
     */
    protected inline fun <reified T: AstNode> children(startIndex: Int = 0, endIndex: Int = 0) = NodeDelegateList<T>({ it is T }, startIndex, endIndex)
    protected fun children(filter: (AstNode) -> Boolean, startIndex: Int = 0, endIndex: Int = 0) = NodeDelegateList<AstNode>(filter, startIndex, endIndex)
    protected class NodeDelegateList<T: AstNode>(val filter: (AstNode) -> Boolean, val startIndex: Int, val endIndex: Int) {
        operator fun getValue(thisRef: AstNode, property: KProperty<*>): List<T> {
            if (!thisRef.valid) throw IllegalStateException("Cannot access children of invalid node")
            val trueStart = if (startIndex <= 0) thisRef.children.size + startIndex else startIndex
            val trueEnd = if (endIndex <= 0) thisRef.children.size + endIndex else endIndex
            val count = trueEnd - trueStart + 1
            return thisRef.children.asSequence()
                .drop(trueStart)
                .take(count)
                .filter(filter)
                .toList() as List<T>
        }
    }
}
