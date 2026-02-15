package app.ultradev.hytaleuiparser.renderer.layout

import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement

object LayoutTools {
    data class FlexMetrics(
        val totalFlexWeight: Int,
        val totalNonFlexSize: Int,
        val sizePerFlexWeight: Int
    ) {
        val totalSize get() = totalNonFlexSize + totalFlexWeight * sizePerFlexWeight
    }

    data class ElementFlexInfo(
        val isFlex: Boolean,
        val size: Int,
        val relativeStart: Int,
        val relativeEnd: Int,
    ) {
        val relativeSize: Int get() = relativeEnd - relativeStart
    }

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
            val flexWeight = child.properties.flexWeight ?: 0
            if (flexWeight != 0) {
                totalFlexWeight += flexWeight
            } else {
                totalNonFlexSize += totalSize(child)
            }
        }
        val sizePerFlexWeight = if (totalFlexWeight > 0) {
            ((availableSize - totalNonFlexSize) / totalFlexWeight).coerceAtLeast(0)
        } else 0
        return FlexMetrics(totalFlexWeight, totalNonFlexSize, sizePerFlexWeight)
    }

    fun computeFlex(
        child: AbstractUIElement,
        sizePerFlexWeight: Int,
        desiredSize: (AbstractUIElement) -> Int,
        totalSize: (AbstractUIElement) -> Int,
        startOffset: Int?,
        endOffset: Int?,
        size: Int?,
    ): ElementFlexInfo {
        val flexWeight = child.properties.flexWeight ?: 0
        if (flexWeight == 0) {
            val computedSize = desiredSize(child)
            val startPoint = startOffset ?: 0
            val endPoint = startPoint + computedSize
            return ElementFlexInfo(
                false,
                totalSize(child),
                startPoint,
                endPoint
            )
        }

        val flexSize = sizePerFlexWeight * flexWeight
        val (relativeStart, relativeEnd) = resolveAxis(
            0,
            flexSize,
            startOffset,
            endOffset,
            size
        )
        return ElementFlexInfo(
            true,
            flexSize,
            relativeStart,
            relativeEnd,
        )
    }
}
