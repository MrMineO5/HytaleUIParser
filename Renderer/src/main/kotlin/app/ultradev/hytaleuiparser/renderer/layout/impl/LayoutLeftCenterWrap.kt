package app.ultradev.hytaleuiparser.renderer.layout.impl

import app.ultradev.hytaleuiparser.renderer.BoxSize
import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.extensions.leftFallback
import app.ultradev.hytaleuiparser.renderer.extensions.maxOfOrZero
import app.ultradev.hytaleuiparser.renderer.extensions.rightFallback
import app.ultradev.hytaleuiparser.renderer.layout.Layout
import app.ultradev.hytaleuiparser.renderer.layout.LayoutTools

object LayoutLeftCenterWrap : Layout {
    private data class Row(
        val children: MutableList<AbstractUIElement> = mutableListOf(),
        var totalWidth: Int = 0,
        var maxTotalHeight: Int = 0
    )

    private fun splitIntoRows(available: BoxSize, children: Sequence<AbstractUIElement>): List<Row> {
        val rows = mutableListOf<Row>()
        var current = Row()

        children.forEach { child ->
            val totalSize = child.totalSize(available)
            val childWidth = totalSize.width
            val height = totalSize.height
            if (current.children.isNotEmpty() && current.totalWidth + childWidth > available.width) {
                rows += current
                current = Row()
            }
            current.children += child
            current.totalWidth += childWidth
            if (height > current.maxTotalHeight) current.maxTotalHeight = height
        }
        if (current.children.isNotEmpty()) rows += current

        return rows
    }

    override fun doLayout(element: BranchUIElement) {
        val cbox = element.contentBox
        val boxSize = BoxSize.fromRenderBox(cbox)
        val rows = splitIntoRows(boxSize, element.visibleChildren)
        var y = cbox.y
        rows.forEach { row ->
            val rowBox = boxSize.copy(height = row.maxTotalHeight)

            val flexMetrics = LayoutTools.flexMetrics(
                row.children.asSequence(),
                cbox.width,
                totalSize = { it.totalSize(rowBox).width }
            )

            var x = cbox.x + (cbox.width - flexMetrics.totalSize) / 2
            row.children.forEach { child ->
                val info = LayoutTools.computeFlex(
                    child,
                    flexMetrics.sizePerFlexWeight,
                    totalSize = { it.totalSize(rowBox).width },
                    desiredSize = { it.desiredSizeFromTotal(rowBox).width },
                    startOffset = child.properties.anchor?.leftFallback(),
                    endOffset = child.properties.anchor?.rightFallback(),
                    size = child.properties.anchor?.width
                )

                val (cy, endY) = LayoutTools.resolveAxis(
                    y,
                    y + row.maxTotalHeight,
                    child.properties.anchor?.top,
                    child.properties.anchor?.bottom,
                    child.desiredSize(rowBox).height
                )

                child.box = RenderBox(x + info.relativeStart, cy, info.relativeSize, endY - cy)
                x += info.size
            }

            y += row.maxTotalHeight
        }
    }

    override val combineMode: BoxSize.BoxCombineMode
        get() = error("Combine mode should not be used directly")

    override fun contentDesiredSize(element: BranchUIElement, available: BoxSize): BoxSize {
        val rows = splitIntoRows(available, element.visibleChildren)
        return BoxSize(
            rows.maxOfOrZero { it.totalWidth },
            rows.sumOf { it.maxTotalHeight }
        )
    }
}
