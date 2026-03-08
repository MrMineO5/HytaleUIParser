package app.ultradev.hytaleuiparser.renderer.cache

import app.ultradev.hytaleuiparser.source.AssetSourceProvider
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension

class JsonAssetCache<T>(
    assetSourceProvider: AssetSourceProvider,
    val serializer: KSerializer<T>,
    val path: String,
) : RenderCache(assetSourceProvider) {
    val cache = mutableMapOf<String, T>()
    private val fileIndex = mutableMapOf<String, Path>()
    private var firstRun = true

    operator fun get(id: String): T {
        if (id in cache) return cache[id]!!

        if (firstRun) {
            firstRun = false
            fileIndex.putAll(
                source.listAllFiles()
                    .filter { it.extension == "json" && it.startsWith("Server/$path") }
                    .associateBy { it.nameWithoutExtension })
        }

        val assetPath = fileIndex[id] ?: error("Could not find asset for id: $id")

        val asset = source.getAsset(assetPath) ?: error("Could not read asset: $assetPath")
        val value = json.decodeFromString(serializer, asset.reader().readText())
        cache[id] = value
        return value
    }

    override fun invalidate() {
        cache.clear()
    }

    companion object {
        val json = Json {
            ignoreUnknownKeys = true
        }

        inline operator fun <reified T> invoke(assetSourceProvider: AssetSourceProvider, path: String) =
            JsonAssetCache(assetSourceProvider, serializer<T>(), path)
    }
}