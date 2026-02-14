package app.ultradev.hytaleuiparser.renderer.layout.impl

import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.extensions.leftFallback
import app.ultradev.hytaleuiparser.renderer.extensions.maxOfOrZero
import app.ultradev.hytaleuiparser.renderer.extensions.rightFallback
import app.ultradev.hytaleuiparser.renderer.layout.Layout
import app.ultradev.hytaleuiparser.renderer.layout.LayoutTools

object LayoutLeft : Layout {
    override fun doLayout(element: BranchUIElement) {
        val cbox = element.contentBox

        var totalFlexWeight = 0
        var totalNonFlexWidth = 0
        element.visibleChildren.forEach { child ->
            if (child.properties.flexWeight != null) {
                totalFlexWeight += child.properties.flexWeight!!
            } else {
                totalNonFlexWidth += child.totalWidth()
            }
        }
        // TODO: Rounding could be a problem for small width
        val widthPerFlexWeight = if (totalFlexWeight > 0) (cbox.width - totalNonFlexWidth) / totalFlexWeight else 0

        var x = cbox.x
        element.visibleChildren.forEach { child ->
            val width = if (child.properties.flexWeight == null) {
                child.desiredWidth()
            } else {
                widthPerFlexWeight * child.properties.flexWeight!! - child.totalWidth() + child.desiredWidth()
            }

            val (y, endY) = LayoutTools.resolveAxis(
                cbox.y,
                cbox.y + cbox.height,
                child.properties.anchor?.top,
                child.properties.anchor?.bottom,
                child.properties.anchor?.height
            )

            x += child.properties.anchor?.leftFallback() ?: 0
            child.box = RenderBox(x, y, width, endY - y)
            x += child.properties.anchor?.rightFallback() ?: 0

            x += width
        }
    }

    override fun contentDesiredHeight(element: BranchUIElement): Int = element.visibleChildren.maxOfOrZero { it.totalHeight() }
    override fun contentDesiredWidth(element: BranchUIElement): Int = element.visibleChildren.sumOf { it.totalWidth() }
}