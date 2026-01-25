package app.ultradev.hytaleuiparser.validation

import java.nio.file.Path

fun String.resolveNeighbour(relative: String): String {
    val path = Path.of(this)
    if (path.parent == null) return Path.of(relative).toString()
    return path.parent.resolve(relative).normalize().toString()
}