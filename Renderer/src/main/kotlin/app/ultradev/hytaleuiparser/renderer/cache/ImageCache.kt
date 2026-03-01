package app.ultradev.hytaleuiparser.renderer.cache

import app.ultradev.hytaleuiparser.renderer.render.RenderImage
import app.ultradev.hytaleuiparser.source.AssetSourceProvider
import java.awt.Image
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.io.path.name

class ImageCache(sourceProvider: AssetSourceProvider) : RenderCache(sourceProvider) {
    val images = mutableMapOf<String, RenderImage>()

    operator fun get(relativePath: String): RenderImage {
        if (relativePath in images) return images[relativePath]!!
        var path = Path(relativePath)
        var inputStream = source.getAsset(path)
        val image = if (inputStream == null) {
            path = path.resolveSibling(
                path.name.substringBeforeLast(".") + "@2x." + path.name.substringAfterLast(".")
            )
            inputStream = source.getAsset(path)
            if (inputStream == null) error("Couldn't find texture: $path")
            val image = ImageIO.read(inputStream)
            RenderImage(image, 2)
        } else {
            RenderImage(ImageIO.read(inputStream), 1)
        }

        images[relativePath] = image
        return image
    }

    override fun invalidate() {
        images.clear()
    }
}