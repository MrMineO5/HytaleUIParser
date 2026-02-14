package app.ultradev.hytaleuiparser.renderer.layout.impl

import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.extensions.bottomFallback
import app.ultradev.hytaleuiparser.renderer.extensions.topFallback
import app.ultradev.hytaleuiparser.renderer.layout.Layout
import app.ultradev.hytaleuiparser.renderer.layout.LayoutTools

object LayoutTop : Layout {
    override fun doLayout(element: BranchUIElement) {
        val cbox = element.contentBox

        var y = cbox.y
        element.children.forEach { child ->
            val height = child.desiredHeight()

            val (x, endX) = LayoutTools.resolveAxis(
                cbox.x,
                cbox.x + cbox.width,
                child.properties.anchor?.left,
                child.properties.anchor?.right,
                child.properties.anchor?.width
            )

            y += child.properties.anchor?.top ?: child.properties.anchor?.vertical ?: child.properties.anchor?.full ?: 0
            child.box = RenderBox(x, y, endX - x, height)
            y += child.properties.anchor?.bottom ?: child.properties.anchor?.vertical ?: child.properties.anchor?.full
                    ?: 0

            y += height
        }
    }

    override fun contentDesiredHeight(element: BranchUIElement): Int = element.children.sumOf {
        it.desiredHeight() + (it.properties.anchor?.topFallback() ?: 0) + (it.properties.anchor?.bottomFallback() ?: 0)
    }

    override fun contentDesiredWidth(element: BranchUIElement): Int =
        element.children.maxOfOrNull { it.desiredWidth() } ?: 0
}