package app.ultradev.hytaleuiparser.renderer.context

import app.ultradev.hytaleuiparser.renderer.render.RenderImage
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import app.ultradev.hytaleuiparser.renderer.type.Point
import app.ultradev.hytaleuiparser.renderer.type.RenderBox
import java.awt.Color
import java.util.*

class DrawContext : RenderTarget {
    private lateinit var target0: RenderTarget
    fun setTarget(target: RenderTarget) {
        this.target0 = target
    }

    val offsets: Stack<Point> = Stack()
    val clips: Stack<RenderBox> = Stack()

    fun pushOffset(x: Int, y: Int) {
        var point = Point(x, y)
        if (offsets.isNotEmpty()) {
            point += offsets.peek()
        }
        offsets.push(point)
    }

    fun popOffset() {
        offsets.pop()
    }

    fun applyOffset(box: RenderBox): RenderBox {
        return if (offsets.isEmpty()) {
            box
        } else {
            box.shift(offsets.peek())
        }
    }

    fun pushClip(box: RenderBox) {
        var newBox = applyOffset(box)
        if (clips.isNotEmpty()) {
            newBox = newBox.intersect(clips.peek())
        }

        clips.push(newBox)
        target0.setClip(clips.peek())
    }
    fun popClip() {
        clips.pop()
        target0.setClip(if (clips.isEmpty()) null else clips.peek())
    }


    // Render Target functions
    override val windowBounds: RenderBox
        get() = target0.windowBounds

    override fun renderFill(color: Color, box: RenderBox) {
        target0.renderFill(color, applyOffset(box))
    }

    override fun renderImage(
        image: RenderImage,
        box: RenderBox,
        horizontalBorder: Int,
        verticalBorder: Int
    ) {
        target0.renderImage(image, applyOffset(box), horizontalBorder, verticalBorder)
    }

    override fun renderText(text: String, box: RenderBox, info: TextRenderStyle) {
        target0.renderText(text, applyOffset(box), info)
    }

    override fun setClip(box: RenderBox?) = throw IllegalArgumentException("Should not be called")
}