package app.ultradev.hytaleuiparser.renderer.layout.impl

import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.extensions.bottomFallback
import app.ultradev.hytaleuiparser.renderer.extensions.maxOfOrZero
import app.ultradev.hytaleuiparser.renderer.extensions.topFallback
import app.ultradev.hytaleuiparser.renderer.layout.Layout
import app.ultradev.hytaleuiparser.renderer.layout.LayoutTools

object LayoutMiddle : Layout {
    override fun doLayout(element: BranchUIElement) {
        val cbox = element.contentBox

        val flexMetrics = LayoutTools.flexMetrics(
            element.visibleChildren,
            cbox.height,
            totalSize = { it.totalHeight(cbox.height) }
        )

        var y = cbox.y + (cbox.height - flexMetrics.totalSize) / 2
        element.visibleChildren.forEach { child ->
            val info = LayoutTools.computeFlex(
                child,
                flexMetrics.sizePerFlexWeight,
                totalSize = { it.totalHeight(cbox.height) },
                desiredSize = { it.desiredHeightFromTotal(cbox.height) },
                startOffset = child.properties.anchor?.topFallback(),
                endOffset = child.properties.anchor?.bottomFallback(),
                size = child.properties.anchor?.height
            )

            val (x, endX) = LayoutTools.resolveAxis(
                cbox.x,
                cbox.x + cbox.width,
                child.properties.anchor?.left,
                child.properties.anchor?.right,
                child.properties.anchor?.width
            )

            child.box = RenderBox(x, y + info.relativeStart, endX - x, info.relativeSize)

            y += info.size
        }
    }

    override fun contentDesiredHeight(element: BranchUIElement, available: Int): Int = element.visibleChildren.sumOf { it.totalHeight(available) }
    override fun contentDesiredWidth(element: BranchUIElement, available: Int): Int = element.visibleChildren.maxOfOrZero { it.totalWidth(available) }
}
