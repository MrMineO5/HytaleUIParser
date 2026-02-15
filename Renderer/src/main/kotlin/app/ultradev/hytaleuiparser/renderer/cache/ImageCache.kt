package app.ultradev.hytaleuiparser.renderer.cache

import app.ultradev.hytaleuiparser.renderer.TestRenderer
import java.awt.Image
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.io.path.exists
import kotlin.io.path.name

class ImageCache {
    val images = mutableMapOf<String, BufferedImage>()

    operator fun get(relativePath: String): BufferedImage {
        if (relativePath in images) return images[relativePath]!!

        val path = TestRenderer.basePath.resolve(relativePath)
        val image = if (!path.exists()) {
            // try @2x and scale it
            // TODO: Handle antialiasing
            val newPath =
                path.resolveSibling(path.name.substringBeforeLast(".") + "@2x." + path.name.substringAfterLast("."))
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

        images[relativePath] = image
        return image
    }
}