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
    private const val FORCE_ANCHOR_WIDTH = true

    private fun resolvedWidth(child: AbstractUIElement): Int {
        if (FORCE_ANCHOR_WIDTH || child.properties.anchor?.width == null) return child.desiredWidth()
        val baseWidth = child.contentDesiredWidth()
        val pl = child.properties.padding?.leftFallback() ?: 0
        val pr = child.properties.padding?.rightFallback() ?: 0
        return baseWidth + pl + pr
    }

    private data class Row(
        val children: MutableList<AbstractUIElement> = mutableListOf(),
        var totalWidth: Int = 0,
        var maxTotalHeight: Int = 0
    )

    override fun doLayout(element: BranchUIElement) {
        val cbox = element.contentBox
        val rows = mutableListOf<Row>()
        var current = Row()

        element.visibleChildren.forEach { child ->
            val childWidth =
                resolvedWidth(child) +
                    (child.properties.anchor?.leftFallback() ?: 0) +
                    (child.properties.anchor?.rightFallback() ?: 0)
            val childHeight = child.totalHeight()
            if (current.children.isNotEmpty() && current.totalWidth + childWidth > cbox.width) {
                rows += current
                current = Row()
            }
            current.children += child
            current.totalWidth += childWidth
            if (childHeight > current.maxTotalHeight) current.maxTotalHeight = childHeight
        }
        if (current.children.isNotEmpty()) rows += current

        var y = cbox.y
        rows.forEach { row ->
            /*
             * LeftCenterWrap spacing ... janky game behaviours...
             * - Rows are centered based on base widths when all weights are 0.
             * - When any weight exists, we still center the base widths first, then add extra space.
             * - Extra space can overhang the parent; it is applied only before weighted items.
             * - The per-weight unit is half of the base remaining width, scaled by FlexWeight.
             */
            var totalFlexWeight = 0
            row.children.forEach { child ->
                totalFlexWeight += child.properties.flexWeight ?: 0
            }
            val baseRemaining = cbox.width - row.totalWidth
            val weightUnit = if (baseRemaining > 0 && totalFlexWeight > 0) baseRemaining / 2 else 0
            var x = cbox.x + if (totalFlexWeight > 0) 0 else baseRemaining / 2

            row.children.forEach { child ->
                val weight = child.properties.flexWeight ?: 0
                val beforeExtra = if (weightUnit > 0 && weight > 0) weightUnit * weight else 0
                val width = resolvedWidth(child)

                val (cy, endY) = LayoutTools.resolveAxis(
                    y,
                    y + row.maxTotalHeight,
                    child.properties.anchor?.top,
                    child.properties.anchor?.bottom,
                    child.desiredHeight()
                )

                x += beforeExtra
                x += child.properties.anchor?.leftFallback() ?: 0
                child.box = RenderBox(x, cy, width, endY - cy)
                x += width
                x += child.properties.anchor?.rightFallback() ?: 0
            }

            y += row.maxTotalHeight
        }
    }

    override fun contentDesiredHeight(element: BranchUIElement): Int =
        element.visibleChildren.maxOfOrZero { it.totalHeight() }

    override fun contentDesiredWidth(element: BranchUIElement): Int =
        element.visibleChildren.sumOf { it.totalWidth() }
}
