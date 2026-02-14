package app.ultradev.hytaleuiparser.renderer.extensions

fun <T> Iterable<T>.maxOfOrZero(selector: (T) -> Int) = maxOfOrNull(selector) ?: 0