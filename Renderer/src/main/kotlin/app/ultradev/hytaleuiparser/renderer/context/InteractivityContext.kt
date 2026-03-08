package app.ultradev.hytaleuiparser.renderer.context

import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.type.Point
import app.ultradev.hytaleuiparser.renderer.type.RenderBox
import java.awt.Cursor

class InteractivityContext(
    // TODO: We really shouldn't be doing this, if you think of a better way, FIX IT
    private val drawContext: DrawContext
) {
    var previousMousePosition: Point = Point(0, 0)
    var mousePosition: Point = Point(0, 0)

    var active: AbstractUIElement? = null

    var setCursor: (Cursor) -> Unit = {}

    fun updateMousePosition(point: Point) {
        previousMousePosition = mousePosition
        mousePosition = point
    }

    fun mouseInside(box: RenderBox) = drawContext.applyOffset(box).contains(mousePosition.x, mousePosition.y)

    fun movedInto(box: RenderBox): Boolean {
        val box = drawContext.applyOffset(box)
        return box.contains(mousePosition.x, mousePosition.y)
                && !box.contains(previousMousePosition.x, previousMousePosition.y)
    }
    fun movedOutOf(box: RenderBox): Boolean {
        val box = drawContext.applyOffset(box)
        return box.contains(previousMousePosition.x, previousMousePosition.y)
                && !box.contains(mousePosition.x, mousePosition.y)
    }

    fun reset() {
        active = null
    }
}