package app.ultradev.hytaleuiparser.renderer.cache

import app.ultradev.hytaleuiparser.renderer.TestRenderer
import java.awt.Image
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.io.path.name

class ImageCache {
    val images = mutableMapOf<String, BufferedImage>()

    operator fun get(relativePath: String): BufferedImage {
        if (relativePath in images) return images[relativePath]!!
        var path = Path(relativePath)
        var inputStream = TestRenderer.source.getAsset(path)
        val image = if (inputStream == null) {
            // try @2x and scale it
            // TODO: Handle antialiasing
            path = path.resolveSibling(
                path.name.substringBeforeLast(".") + "@2x." + path.name.substringAfterLast(".")
            )
            inputStream = TestRenderer.source.getAsset(path)
            if (inputStream == null) error("Couldn't find texture: $path")
            val image = ImageIO.read(inputStream)
            val new = BufferedImage(image.width / 2, image.height / 2, BufferedImage.TYPE_INT_ARGB)
            val g = new.createGraphics()
            g.drawImage(image.getScaledInstance(image.width / 2, image.height / 2, Image.SCALE_FAST), 0, 0, null)
            g.dispose()
            new
        } else {
            ImageIO.read(inputStream)
        }

        images[relativePath] = image
        return image
    }
}