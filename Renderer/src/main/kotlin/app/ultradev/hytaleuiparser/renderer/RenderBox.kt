package app.ultradev.hytaleuiparser.renderer

import app.ultradev.hytaleuiparser.generated.types.Anchor
import app.ultradev.hytaleuiparser.generated.types.Padding
import kotlin.math.max

data class RenderBox(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
) {

    fun withAnchor(anchor: Anchor): RenderBox {
        val parentStartX = x
        val parentEndX = x + width
        val parentStartY = y
        val parentEndY = y + height

        val left: Int? = anchor.left ?: anchor.horizontal ?: anchor.full
        val right: Int? = anchor.right ?: anchor.horizontal ?: anchor.full
        val top: Int? = anchor.top ?: anchor.vertical ?: anchor.full
        val bottom: Int? = anchor.bottom ?: anchor.vertical ?: anchor.full

        val (newX, newEndX) = resolveAxis(
            parentStart = parentStartX,
            parentEnd = parentEndX,
            startOffset = left,
            endOffset = right,
            size = anchor.width
        )

        val (newY, newEndY) = resolveAxis(
            parentStart = parentStartY,
            parentEnd = parentEndY,
            startOffset = top,
            endOffset = bottom,
            size = anchor.height
        )

        return RenderBox(
            x = newX,
            y = newY,
            width = max(0, newEndX - newX),
            height = max(0, newEndY - newY)
        )
    }

    fun withPadding(padding: Padding): RenderBox {
        val top = padding.top ?: padding.vertical ?: padding.full ?: 0
        val bottom = padding.bottom ?: padding.vertical ?: padding.full ?: 0
        val left = padding.left ?: padding.horizontal ?: padding.full ?: 0
        val right = padding.right ?: padding.horizontal ?: padding.full ?: 0
        return RenderBox(x + left, y + top, width - left - right, height - top - bottom)
    }

    private fun resolveAxis(
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
