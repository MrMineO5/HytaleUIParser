package app.ultradev.hytaleuiparser.renderer.layout.impl

import app.ultradev.hytaleuiparser.renderer.Axis
import app.ultradev.hytaleuiparser.renderer.BoxSize
import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.extensions.bottomFallback
import app.ultradev.hytaleuiparser.renderer.extensions.maxOfOrZero
import app.ultradev.hytaleuiparser.renderer.extensions.topFallback
import app.ultradev.hytaleuiparser.renderer.layout.Layout
import app.ultradev.hytaleuiparser.renderer.layout.LayoutTools

object LayoutTop : Layout {
    override fun doLayout(element: BranchUIElement) {
        val cbox = element.contentBox
        val boxSize = BoxSize.fromRenderBox(cbox)

        val flexMetrics = LayoutTools.flexMetrics(element.visibleChildren, boxSize, Axis.VERTICAL)

        var y = cbox.y
        element.visibleChildren.forEach { child ->
            val info = LayoutTools.computeFlex(
                child,
                flexMetrics.sizePerFlexWeight,
                boxSize,
                Axis.VERTICAL
            )

            val (x, endX) = LayoutTools.resolveAxis(child, cbox, Axis.HORIZONTAL)

            child.box = RenderBox(x, y + info.relativeStart, endX - x, info.relativeSize)

            y += info.size
        }
    }

    override val combineMode = BoxSize.BoxCombineMode(
        BoxSize.AxisCombineMode.MAX_OR_ZERO,
        BoxSize.AxisCombineMode.SUM
    )
}
