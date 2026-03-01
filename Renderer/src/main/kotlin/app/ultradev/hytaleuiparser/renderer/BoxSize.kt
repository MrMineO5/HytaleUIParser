package app.ultradev.hytaleuiparser.renderer

import kotlin.math.max

data class BoxSize(
    val width: Int,
    val height: Int,
) {
    operator fun plus(other: BoxSize) = BoxSize(this.width + other.width, this.height + other.height)
    operator fun minus(other: BoxSize) = BoxSize(this.width - other.width, this.height - other.height)

    enum class AxisCombineMode(val initial: Int, val combine: (Int, Int) -> Int) {
        MAX_OR_ZERO(0, { a, b -> max(a, b) }),
        SUM(0, { a, b -> a + b }),
        ;
    }
    data class BoxCombineMode(val width: AxisCombineMode, val height: AxisCombineMode)

    operator fun get(axis: Axis) = when (axis) {
        Axis.HORIZONTAL -> width
        Axis.VERTICAL -> height
    }

    companion object {
        val ZERO = BoxSize(0, 0)

        fun fromRenderBox(box: RenderBox) = BoxSize(box.width, box.height)

        fun Sequence<BoxSize>.combine(widthMode: AxisCombineMode, heightMode: AxisCombineMode): BoxSize {
            var width = widthMode.initial
            var height = heightMode.initial
            for (size in this) {
                width = widthMode.combine(width, size.width)
                height = heightMode.combine(height, size.height)
            }
            return BoxSize(width, height)
        }
        fun Sequence<BoxSize>.combine(mode: BoxCombineMode): BoxSize {
            return combine(mode.width, mode.height)
        }
    }
}
