package app.ultradev.hytaleuiparser.renderer.extensions

fun <T> Iterable<T>.maxOfOrZero(selector: (T) -> Int) = maxOfOrNull(selector) ?: 0

fun <T> List<T>.insertBefore(element: T, newElements: List<T>): List<T> {
    val index = indexOfFirst { it === element }
    return subList(0, index) + newElements + subList(index, size)
}
