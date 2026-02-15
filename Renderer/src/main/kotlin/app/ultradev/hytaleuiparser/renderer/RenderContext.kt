package app.ultradev.hytaleuiparser.renderer

import app.ultradev.hytaleuiparser.renderer.cache.RenderCache
import java.awt.Point

class RenderContext(
    var isInteractive: Boolean = true,
    var mousePosition: Point = Point(0, 0),
) {
    val cache: RenderCache = RenderCache()

    fun mouseInside(box: RenderBox) = box.contains(mousePosition.x, mousePosition.y)
}
