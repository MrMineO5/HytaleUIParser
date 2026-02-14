package app.ultradev.hytaleuiparser.source

import java.io.InputStream
import java.nio.file.Path

interface AssetSource {
    fun listAssets(): List<Path>
    fun getAsset(path: Path): InputStream?
}