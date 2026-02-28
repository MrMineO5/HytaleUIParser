package app.ultradev.hytaleuiparser.source

import java.io.InputStream
import java.nio.file.Path

object EmptyAssetSource : AssetSource {
    override fun listAllFiles(): List<Path> = emptyList()
    override fun getAsset(path: Path): InputStream? = null
}