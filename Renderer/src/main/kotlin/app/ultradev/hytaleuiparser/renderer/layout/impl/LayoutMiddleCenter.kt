package app.ultradev.hytaleuiparser.renderer.layout.impl

import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.extensions.bottomFallback
import app.ultradev.hytaleuiparser.renderer.extensions.maxOfOrZero
import app.ultradev.hytaleuiparser.renderer.extensions.topFallback
import app.ultradev.hytaleuiparser.renderer.layout.Layout
import app.ultradev.hytaleuiparser.renderer.layout.LayoutTools

object LayoutMiddleCenter : Layout {
    override fun doLayout(element: BranchUIElement) {
        val cbox = element.contentBox

        val flexMetrics = LayoutTools.flexMetrics(
            element.visibleChildren,
            cbox.height,
            totalSize = { it.totalHeight() }
        )
        val totalHeight = flexMetrics.totalNonFlexSize + flexMetrics.totalFlexWeight * flexMetrics.sizePerFlexWeight

        var y = cbox.y + (cbox.height - totalHeight) / 2
        element.visibleChildren.forEach { child ->
            val height = LayoutTools.resolveFlexSize(
                child,
                flexMetrics.sizePerFlexWeight,
                totalSize = { it.totalHeight() },
                desiredSize = { it.desiredHeight() }
            )

            val (x, endX) = LayoutTools.resolveAxis(
                cbox.x,
                cbox.x + cbox.width,
                child.properties.anchor?.left,
                child.properties.anchor?.right,
                child.desiredWidth()
            )

            y += child.properties.anchor?.topFallback() ?: 0
            child.box = RenderBox(x, y, endX - x, height)
            y += height
            y += child.properties.anchor?.bottomFallback() ?: 0
        }
    }

    override fun contentDesiredHeight(element: BranchUIElement): Int = element.visibleChildren.sumOf { it.totalHeight() }
    override fun contentDesiredWidth(element: BranchUIElement): Int = element.visibleChildren.maxOfOrZero { it.totalWidth() }
}
