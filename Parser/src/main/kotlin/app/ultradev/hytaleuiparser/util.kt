package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.ast.MissingNode

fun List<AstNode>.clone() = map { it.clone() }

private fun doInsertMissing(list: MutableList<AstNode?>): List<AstNode> {
    for (i in list.size - 1 downTo 0) {
        if (list[i] == null) list.removeAt(i)
        else break
    }

    for (i in list.size - 2 downTo 0) {
        if (list[i] == null) {
            list[i] = MissingNode.before(list[i+1]!!.tokens.first())
        }
    }
    @Suppress("UNCHECKED_CAST")
    return list as List<AstNode>
}
fun List<AstNode?>.insertMissing(): List<AstNode> = doInsertMissing(toMutableList())
fun listOfInsertMissing(vararg nodes: AstNode?) = doInsertMissing(mutableListOf(*nodes))

fun <T, R> IndexedValue<T>.mapValue(transform: (T) -> R): IndexedValue<R> = IndexedValue(this.index, transform(this.value))
fun <T, R> Sequence<IndexedValue<T>>.mapValue(transform: (T) -> R): Sequence<IndexedValue<R>> = map { it.mapValue(transform) }

fun Int.convertNegativeIndex(list: List<*>): Int {
    return if (this < 0) list.size + this else this
}
fun <T> List<T>.getOrNullAllowNegative(index: Int) = getOrNull(index.convertNegativeIndex(this))
