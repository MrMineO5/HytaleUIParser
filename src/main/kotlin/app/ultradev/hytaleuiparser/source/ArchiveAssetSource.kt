package app.ultradev.hytaleuiparser.source

import java.io.InputStream
import java.nio.file.Path
import java.util.zip.ZipInputStream
import kotlin.io.path.extension
import kotlin.io.path.inputStream

class ArchiveAssetSource(
    val archive: Path
) : AssetSource {
    init {
        assert(archive.extension == "zip" || archive.extension == "jar") {
            "Archive must be a zip or jar file."
        }
    }

    override fun listAssets(): List<Path> {
        val input = ZipInputStream(archive.inputStream())
        val entries = mutableListOf<Path>()
        var entry = input.nextEntry
        while (entry != null) {
            if (entry.name.endsWith(".ui")) {
                entries.add(Path.of(entry.name))
            }
            entry = input.nextEntry
        }
        return entries
    }

    override fun getAsset(path: Path): InputStream? {
        val input = ZipInputStream(archive.inputStream())
        var entry = input.nextEntry
        while (entry != null) {
            if (entry.name == path.toString()) {
                return input
            }
            entry = input.nextEntry
        }
        return null
    }
}