package app.ultradev.hytaleuiparser.source

import java.io.InputStream
import java.nio.file.Path

interface AssetSource {
    fun listUIFiles(): List<Path>
    fun getAsset(path: Path): InputStream?
}