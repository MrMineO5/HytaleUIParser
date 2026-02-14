package app.ultradev.hytaleuiparser.renderer.layout

object LayoutTools {
    fun resolveAxis(
        parentStart: Int,
        parentEnd: Int,
        startOffset: Int?, // null = not pinned
        endOffset: Int?,   // null = not pinned
        size: Int?
    ): Pair<Int, Int> {
        val startPinned = startOffset != null
        val endPinned = endOffset != null

        var s = parentStart + (startOffset ?: 0)
        var e = parentEnd - (endOffset ?: 0)

        if (size != null && !(startPinned && endPinned)) {
            when {
                startPinned -> e = s + size
                endPinned -> s = e - size
                else -> {
                    s = parentStart + (parentEnd - parentStart - size) / 2
                    e = s + size
                }
            }
        }

        if (e < s) e = s
        return s to e
    }
}