package app.ultradev.hytaleuiparser.renderer.element

import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget
import java.awt.Color
import java.awt.image.BufferedImage

data class UIElementBackground(
    val color: Color? = null,
    val image: BufferedImage? = null,

    // TODO: Anchor
    val horizontalBorder: Int = 0,
    val verticalBorder: Int = 0
) {
    fun draw(target: RenderTarget, box: RenderBox) {
        // TODO: Image tinting
        if (image != null) {
            target.renderImage(image, box.x, box.y, box.width, box.height, horizontalBorder, verticalBorder)
        } else if (color != null) {
            target.renderFill(color, box.x, box.y, box.width, box.height)
        }
    }
}
