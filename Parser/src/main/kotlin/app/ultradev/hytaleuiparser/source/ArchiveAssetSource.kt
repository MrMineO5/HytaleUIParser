package app.ultradev.hytaleuiparser.source

import java.io.InputStream
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Path
import kotlin.io.path.*

class ArchiveAssetSource(
    archive: Path
) : AssetSource {
    private val fs: FileSystem

    init {
        assert(archive.extension == "zip" || archive.extension == "jar") {
            "Archive must be a zip or jar file."
        }

        fs = FileSystems.newFileSystem(archive, null as ClassLoader?)
    }

    override fun listUIFiles(): List<Path> {
        val root = fs.rootDirectories.first()
        return root.walk()
            .filter { it.isRegularFile() && it.extension == "ui" }
            .map { it.relativeTo(root) }
            .toList()
    }

    override fun getAsset(path: Path): InputStream? {
        val zipPath = fs.getPath(path.toString())
        return if (zipPath.exists()) {
            zipPath.inputStream()
        } else {
            null
        }
    }
}