package app.ultradev.hytaleuiparser.renderer.element

import app.ultradev.hytaleuiparser.generated.types.PatchStyle
import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.TestRenderer
import app.ultradev.hytaleuiparser.renderer.target.RenderTarget
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.io.path.exists
import kotlin.io.path.name

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

    companion object {
        fun fromPatchStyle(style: PatchStyle): UIElementBackground {
            val image = style.texturePath?.let { texturePath ->
                val path = TestRenderer.basePath.resolve(texturePath)
                if (!path.exists()) {
                    // try @2x and scale it
                    // TODO: Handle antialiasing
                    val newPath = path.resolveSibling(path.name.substringBeforeLast(".") + "@2x." + path.name.substringAfterLast("."))
                    if (!newPath.exists()) error("Couldn't find texture: $newPath")
                    val image = ImageIO.read(newPath.toFile())
                    val new = BufferedImage(image.width / 2, image.height / 2, BufferedImage.TYPE_INT_ARGB)
                    val g = new.createGraphics()
                    g.drawImage(image.getScaledInstance(image.width / 2, image.height / 2, Image.SCALE_FAST), 0, 0, null)
                    g.dispose()
                    new
                } else {
                    ImageIO.read(path.toFile())
                }
            }
            return UIElementBackground(
                color = style.color,
                image = image,
                horizontalBorder = style.horizontalBorder ?: style.border ?: 0,
                verticalBorder = style.verticalBorder ?: style.border ?: 0,
            )
        }
    }
}
