package app.ultradev.hytaleuiparser.source

import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.extension
import kotlin.io.path.inputStream
import kotlin.io.path.isRegularFile
import kotlin.io.path.relativeTo
import kotlin.io.path.walk

class DirectoryAssetSource(
    val directory: Path
) : AssetSource {
    override fun listUIFiles(): List<Path> {
        return directory.walk()
            .filter { it.isRegularFile() && it.extension == "ui" }
            .map { it.relativeTo(directory) }
            .toList()
    }

    override fun getAsset(path: Path): InputStream? {
        val file = directory.resolve(path)
        if (!file.exists()) return null
        return file.inputStream()
    }
}