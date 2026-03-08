package app.ultradev.hytaleuiparser.renderer.render

import app.ultradev.hytaleuiparser.generated.types.Anchor
import app.ultradev.hytaleuiparser.generated.types.PatchStyle
import app.ultradev.hytaleuiparser.renderer.context.RenderContext
import app.ultradev.hytaleuiparser.renderer.type.RenderBox

fun drawPatchStyle(context: RenderContext, box: RenderBox, style: PatchStyle?) {
    if (style == null) return

    val finalBox = box.withAnchor(style.anchor ?: Anchor.EMPTY)

    // TODO: Image tinting
    if (style.texturePath != null) {
        val image = context.cache.images[style.texturePath!!]
        context.draw.renderImage(
            image,
            finalBox,
            style.horizontalBorder ?: style.border ?: 0,
            style.verticalBorder ?: style.border ?: 0
        )
    } else if (style.color != null) {
        context.draw.renderFill(style.color!!, finalBox)
    }
}
