package app.ultradev.hytaleuiparser.renderer.render

import app.ultradev.hytaleuiparser.generated.types.PatchStyle
import app.ultradev.hytaleuiparser.generated.types.ScrollbarStyle
import app.ultradev.hytaleuiparser.renderer.type.Axis
import app.ultradev.hytaleuiparser.renderer.type.RenderBox
import app.ultradev.hytaleuiparser.renderer.context.RenderContext
import app.ultradev.hytaleuiparser.renderer.extensions.axisAnchor
import java.awt.Color

fun drawScrollBarStyle(
    context: RenderContext,
    box: RenderBox,
    total: Int,
    screen: Int,
    position: Int,
    axis: Axis,
    style: ScrollbarStyle?
) {
    val style = style ?: ScrollbarStyle(
        size = 20,
        spacing = 20,
        background = PatchStyle(color = Color(0xCC434343.toUInt().toInt(), true)),
        handle = PatchStyle(color = Color(0xCCCCCC)),
        hoveredHandle = PatchStyle(color = Color(0xEEEEEE)),
        draggedHandle = PatchStyle(color = Color(0xAAAAAA)),
    )

    val backgroundBox = box.withAnchor(
        axisAnchor(
            axis,
            crossEnd = 0,
            crossSize = style.size ?: 20,
            inlineStart = 0,
            inlineEnd = 0,
        )
    )
    drawPatchStyle(context, backgroundBox, style.background)

    val handleStart = position * box.size(axis) / total
    val handleEnd = (position + screen) * box.size(axis) / total
    val handleBox = box.withAnchor(
        axisAnchor(
            axis,
            crossEnd = 0,
            crossSize = style.size ?: 20,
            inlineStart = handleStart,
            inlineSize = handleEnd - handleStart
        )
    )
    val styleToUse = if (context.interactivity.mouseInside(handleBox)) {
        style.hoveredHandle
    } else {
        style.handle
    }
    drawPatchStyle(context, handleBox, styleToUse)
}
