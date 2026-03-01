package app.ultradev.hytaleuiparser.renderer.layout.impl

import app.ultradev.hytaleuiparser.renderer.BoxSize
import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.extensions.leftFallback
import app.ultradev.hytaleuiparser.renderer.extensions.maxOfOrZero
import app.ultradev.hytaleuiparser.renderer.extensions.rightFallback
import app.ultradev.hytaleuiparser.renderer.layout.Layout
import app.ultradev.hytaleuiparser.renderer.layout.LayoutTools

object LayoutCenterMiddle : Layout {
    override fun doLayout(element: BranchUIElement) {
        val cbox = element.childBox
        val boxSize = BoxSize.fromRenderBox(cbox)

        val flexMetrics = LayoutTools.flexMetrics(
            element.visibleChildren,
            cbox.width,
            totalSize = { it.totalSize(boxSize).width }
        )

        var x = cbox.x + (cbox.width - flexMetrics.totalSize) / 2
        element.visibleChildren.forEach { child ->
            val info = LayoutTools.computeFlex(
                child,
                flexMetrics.sizePerFlexWeight,
                totalSize = { it.totalSize(boxSize).width },
                desiredSize = { it.desiredSizeFromTotal(boxSize).width },
                startOffset = child.properties.anchor?.leftFallback(),
                endOffset = child.properties.anchor?.rightFallback(),
                size = child.properties.anchor?.width
            )

            val (y, endY) = LayoutTools.resolveAxis(
                cbox.y,
                cbox.y + cbox.height,
                child.properties.anchor?.top,
                child.properties.anchor?.bottom,
                child.desiredSizeFromTotal(boxSize).height
            )

            child.box = RenderBox(x + info.relativeStart, y, info.relativeSize, endY - y)

            x += info.size
        }
    }

    override val combineMode = BoxSize.BoxCombineMode(
        BoxSize.AxisCombineMode.SUM,
        BoxSize.AxisCombineMode.MAX_OR_ZERO
    )
}
