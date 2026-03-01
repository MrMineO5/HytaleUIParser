package app.ultradev.hytaleuiparser.renderer.layout.impl

import app.ultradev.hytaleuiparser.renderer.BoxSize
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
        val boxSize = BoxSize.fromRenderBox(cbox)

        val flexMetrics = LayoutTools.flexMetrics(
            element.visibleChildren,
            cbox.height,
            totalSize = { it.totalSize(boxSize).height }
        )

        var y = cbox.y + (cbox.height - flexMetrics.totalSize) / 2
        element.visibleChildren.forEach { child ->
            val info = LayoutTools.computeFlex(
                child,
                flexMetrics.sizePerFlexWeight,
                totalSize = { it.totalSize(boxSize).height },
                desiredSize = { it.desiredSizeFromTotal(boxSize).height },
                startOffset = child.properties.anchor?.topFallback(),
                endOffset = child.properties.anchor?.bottomFallback(),
                size = child.properties.anchor?.height
            )

            val (x, endX) = LayoutTools.resolveAxis(
                cbox.x,
                cbox.x + cbox.width,
                child.properties.anchor?.left,
                child.properties.anchor?.right,
                child.desiredSizeFromTotal(boxSize).width
            )

            child.box = RenderBox(x, y + info.relativeStart, endX - x, info.relativeSize)

            y += info.size
        }
    }

    override val combineMode = BoxSize.BoxCombineMode(
        BoxSize.AxisCombineMode.MAX_OR_ZERO,
        BoxSize.AxisCombineMode.SUM
    )
}
