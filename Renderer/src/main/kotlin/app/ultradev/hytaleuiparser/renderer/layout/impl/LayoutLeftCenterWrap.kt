package app.ultradev.hytaleuiparser.renderer.layout.impl

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

    private fun splitIntoRows(availableWidth: Int, children: List<AbstractUIElement>): List<Row> {
        val rows = mutableListOf<Row>()
        var current = Row()

        children.forEach { child ->
            val childWidth = child.totalWidth(availableWidth)
            val height = child.totalHeight(100) // TODO: how do we even?
            if (current.children.isNotEmpty() && current.totalWidth + childWidth > availableWidth) {
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
        val rows = splitIntoRows(cbox.width, element.visibleChildren)
        var y = cbox.y
        rows.forEach { row ->
            val flexMetrics = LayoutTools.flexMetrics(
                row.children,
                cbox.width,
                totalSize = { it.totalWidth(cbox.width) }
            )

            var x = cbox.x + (cbox.width - flexMetrics.totalSize) / 2
            row.children.forEach { child ->
                val info = LayoutTools.computeFlex(
                    child,
                    flexMetrics.sizePerFlexWeight,
                    totalSize = { it.totalWidth(cbox.width) },
                    desiredSize = { it.desiredWidthFromTotal(cbox.width) },
                    startOffset = child.properties.anchor?.leftFallback(),
                    endOffset = child.properties.anchor?.rightFallback(),
                    size = child.properties.anchor?.width
                )

                val (cy, endY) = LayoutTools.resolveAxis(
                    y,
                    y + row.maxTotalHeight,
                    child.properties.anchor?.top,
                    child.properties.anchor?.bottom,
                    child.desiredHeight(100) // TODO: how do we even?
                )

                child.box = RenderBox(x + info.relativeStart, cy, info.relativeSize, endY - cy)
                x += info.size
            }

            y += row.maxTotalHeight
        }
    }

    override fun contentDesiredHeight(element: BranchUIElement, available: Int): Int =
        element.visibleChildren.maxOfOrZero { it.totalHeight(available) }

    override fun contentDesiredWidth(element: BranchUIElement, available: Int): Int {
        val rows = splitIntoRows(available, element.visibleChildren)
        return rows.maxOfOrZero { it.totalWidth }
    }
}
