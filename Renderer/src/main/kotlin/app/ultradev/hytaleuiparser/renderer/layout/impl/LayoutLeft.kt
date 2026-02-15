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

        val flexMetrics = LayoutTools.flexMetrics(
            element.visibleChildren,
            cbox.width,
            totalSize = { it.totalWidth() }
        )

        var x = cbox.x
        element.visibleChildren.forEach { child ->
            val width = LayoutTools.resolveFlexSize(
                child,
                flexMetrics.sizePerFlexWeight,
                totalSize = { it.totalWidth() },
                desiredSize = { it.desiredWidth() }
            )

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
