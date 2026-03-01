package app.ultradev.hytaleuiparser.renderer.extensions

import app.ultradev.hytaleuiparser.generated.types.Anchor
import app.ultradev.hytaleuiparser.renderer.Axis

fun Anchor.leftFallback(): Int? = this.left ?: this.horizontal ?: this.full
fun Anchor.rightFallback(): Int? = this.right ?: this.horizontal ?: this.full
fun Anchor.topFallback(): Int? = this.top ?: this.vertical ?: this.full
fun Anchor.bottomFallback(): Int? = this.bottom ?: this.vertical ?: this.full
fun Anchor.startFallback(axis: Axis): Int? = when (axis) {
    Axis.HORIZONTAL -> this.leftFallback()
    Axis.VERTICAL -> this.topFallback()
}
fun Anchor.endFallback(axis: Axis): Int? = when (axis) {
    Axis.HORIZONTAL -> this.rightFallback()
    Axis.VERTICAL -> this.bottomFallback()
}
fun Anchor.size(axis: Axis): Int? = when (axis) {
    Axis.HORIZONTAL -> this.width
    Axis.VERTICAL -> this.height
}
fun Anchor.start(axis: Axis): Int? = when (axis) {
    Axis.HORIZONTAL -> this.left
    Axis.VERTICAL -> this.top
}
fun Anchor.end(axis: Axis): Int? = when (axis) {
    Axis.HORIZONTAL -> this.right
    Axis.VERTICAL -> this.bottom
}

fun axisAnchor(
    axis: Axis,

    inlineSize: Int? = null,
    crossSize: Int? = null,

    inlineStart: Int? = null,
    inlineEnd: Int? = null,
    crossStart: Int? = null,
    crossEnd: Int? = null,
): Anchor {
    return when (axis) {
        Axis.HORIZONTAL -> Anchor(
            left = inlineStart,
            right = inlineEnd,
            width = inlineSize,

            top = crossStart,
            bottom = crossEnd,
            height = crossSize,
        )
        Axis.VERTICAL -> Anchor(
            top = inlineStart,
            bottom = inlineEnd,
            height = inlineSize,

            left = crossStart,
            right = crossEnd,
            width = crossSize,
        )
    }
}


