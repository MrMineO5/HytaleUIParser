package app.ultradev.hytaleuiparser.renderer.layout

import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement

object LayoutTools {
    data class FlexMetrics(
        val totalFlexWeight: Int,
        val totalNonFlexSize: Int,
        val sizePerFlexWeight: Int
    )

    fun resolveAxis(
        parentStart: Int,
        parentEnd: Int,
        startOffset: Int?, // null = not pinned
        endOffset: Int?,   // null = not pinned
        size: Int?
    ): Pair<Int, Int> {
        val startPinned = startOffset != null
        val endPinned = endOffset != null

        var s = parentStart + (startOffset ?: 0)
        var e = parentEnd - (endOffset ?: 0)

        if (size != null && !(startPinned && endPinned)) {
            when {
                startPinned -> e = s + size
                endPinned -> s = e - size
                else -> {
                    s = parentStart + (parentEnd - parentStart - size) / 2
                    e = s + size
                }
            }
        }

        if (e < s) e = s
        return s to e
    }

    fun flexMetrics(
        children: Iterable<AbstractUIElement>,
        availableSize: Int,
        totalSize: (AbstractUIElement) -> Int
    ): FlexMetrics {
        var totalFlexWeight = 0
        var totalNonFlexSize = 0
        children.forEach { child ->
            val flexWeight = child.properties.flexWeight
            if (flexWeight != null) {
                totalFlexWeight += flexWeight
            } else {
                totalNonFlexSize += totalSize(child)
            }
        }
        val sizePerFlexWeight =
            if (totalFlexWeight > 0) (availableSize - totalNonFlexSize) / totalFlexWeight else 0
        return FlexMetrics(totalFlexWeight, totalNonFlexSize, sizePerFlexWeight)
    }

    fun resolveFlexSize(
        child: AbstractUIElement,
        sizePerFlexWeight: Int,
        totalSize: (AbstractUIElement) -> Int,
        desiredSize: (AbstractUIElement) -> Int
    ): Int {
        val flexWeight = child.properties.flexWeight ?: return desiredSize(child)
        return sizePerFlexWeight * flexWeight - totalSize(child) + desiredSize(child)
    }
}
