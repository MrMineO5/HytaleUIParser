package app.ultradev.hytaleuiparser.source

import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.extension

interface AssetSource : AssetSourceProvider {
    override val assetSource: AssetSource get() = this

    fun listUIFiles(): List<Path> = listAllFiles().filter { it.extension == "ui" }
    fun listAllFiles(): List<Path>
    fun getAsset(path: Path): InputStream?

    operator fun plus(other: AssetSource): AssetSource {
        val thisSources = if (this is CombinedAssetSource) sources else listOf(this)
        val otherSources = if (other is CombinedAssetSource) other.sources else listOf(other)
        return CombinedAssetSource(thisSources + otherSources)
    }
}