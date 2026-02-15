package app.ultradev.hytaleuiparser.renderer.render

import app.ultradev.hytaleuiparser.generated.types.Anchor
import app.ultradev.hytaleuiparser.generated.types.PatchStyle
import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.RenderContext
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget

fun drawPatchStyle(target: RenderTarget, context: RenderContext, box: RenderBox, style: PatchStyle?) {
    if (style == null) return

    val finalBox = box.withAnchor(style.anchor ?: Anchor.EMPTY)

    // TODO: Image tinting
    if (style.texturePath != null) {
        val image = context.cache.images[style.texturePath!!]
        target.renderImage(
            image,
            finalBox.x, finalBox.y,
            finalBox.width, finalBox.height,
            style.horizontalBorder ?: style.border ?: 0,
            style.verticalBorder ?: style.border ?: 0
            )
    } else if (style.color != null) {
        target.renderFill(style.color!!, finalBox.x, finalBox.y, finalBox.width, finalBox.height)
    }
}
