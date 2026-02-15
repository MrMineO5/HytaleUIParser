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
            totalSize = { it.totalWidth(cbox.width) }
        )

        var x = cbox.x
        element.visibleChildren.forEach { child ->
            val info = LayoutTools.computeFlex(
                child,
                flexMetrics.sizePerFlexWeight,
                desiredSize = { it.desiredWidthFromTotal(cbox.width) },
                totalSize = { it.totalWidth(cbox.width) },
                startOffset = child.properties.anchor?.leftFallback(),
                endOffset = child.properties.anchor?.rightFallback(),
                size = child.properties.anchor?.width
            )

            val (y, endY) = LayoutTools.resolveAxis(
                cbox.y,
                cbox.y + cbox.height,
                child.properties.anchor?.top,
                child.properties.anchor?.bottom,
                child.properties.anchor?.height
            )

            child.box = RenderBox(x + info.relativeStart, y, info.relativeSize, endY - y)

            x += info.size
        }
    }

    override fun contentDesiredHeight(element: BranchUIElement, available: Int): Int = element.visibleChildren.maxOfOrZero { it.totalHeight(available) }
    override fun contentDesiredWidth(element: BranchUIElement, available: Int): Int = element.visibleChildren.sumOf { it.totalWidth(available) }
}
