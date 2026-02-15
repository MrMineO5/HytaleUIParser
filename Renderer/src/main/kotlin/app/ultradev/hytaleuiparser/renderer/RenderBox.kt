package app.ultradev.hytaleuiparser.renderer

import app.ultradev.hytaleuiparser.generated.types.Anchor
import app.ultradev.hytaleuiparser.generated.types.Padding
import app.ultradev.hytaleuiparser.renderer.extensions.bottomFallback
import app.ultradev.hytaleuiparser.renderer.extensions.leftFallback
import app.ultradev.hytaleuiparser.renderer.extensions.rightFallback
import app.ultradev.hytaleuiparser.renderer.extensions.topFallback
import app.ultradev.hytaleuiparser.renderer.layout.LayoutTools
import kotlin.math.max

data class RenderBox(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
) {
    fun contains(px: Int, py: Int) = px in x..(x + width) && py in y..(y + height)

    fun withAnchor(anchor: Anchor): RenderBox {
        if (anchor == Anchor.EMPTY) return this

        val (newX, newEndX) = LayoutTools.resolveAxis(
            parentStart = x,
            parentEnd = x + width,
            startOffset = anchor.leftFallback(),
            endOffset = anchor.rightFallback(),
            size = anchor.width
        )

        val (newY, newEndY) = LayoutTools.resolveAxis(
            parentStart = y,
            parentEnd = y + height,
            startOffset = anchor.topFallback(),
            endOffset = anchor.bottomFallback(),
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
}
