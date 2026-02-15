package app.ultradev.hytaleuiparser.source

import java.io.InputStream
import java.nio.file.Path

class CombinedAssetSource(
    val sources: List<AssetSource>
) : AssetSource {
    override fun listUIFiles(): List<Path> {
        val allPaths = sources.map { it.listUIFiles() }

        val out = ArrayList<Path>(allPaths.sumOf { it.size })
        allPaths.forEach { paths ->
            paths.forEach { path ->
                if (out.contains(path)) return@forEach
                out.add(path)
            }
        }
        return out
    }

    override fun getAsset(path: Path): InputStream? {
        for (source in sources) {
            val stream = source.getAsset(path)
            if (stream != null) return stream
        }
        return null
    }

}