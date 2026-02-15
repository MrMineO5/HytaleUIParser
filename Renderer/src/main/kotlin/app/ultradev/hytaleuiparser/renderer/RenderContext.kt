package app.ultradev.hytaleuiparser.renderer

import app.ultradev.hytaleuiparser.renderer.cache.RenderCacheCollection
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import java.awt.Cursor
import java.awt.Point

class RenderContext(
    var isInteractive: Boolean = true,
    var previousMousePosition: Point = Point(0, 0),
    var mousePosition: Point = Point(0, 0),
) {
    var active: AbstractUIElement? = null

    val cache: RenderCacheCollection = RenderCacheCollection()

    var setCursor: (Cursor) -> Unit = {}

    fun mouseInside(box: RenderBox) = box.contains(mousePosition.x, mousePosition.y)

    fun movedInto(box: RenderBox): Boolean {
        return box.contains(mousePosition.x, mousePosition.y)
                && !box.contains(previousMousePosition.x, previousMousePosition.y)
    }
    fun movedOutOf(box: RenderBox): Boolean {
        return box.contains(previousMousePosition.x, previousMousePosition.y)
                && !box.contains(mousePosition.x, mousePosition.y)
    }
}
