package app.ultradev.hytaleuiparser.source.index

import app.ultradev.hytaleuiparser.source.AssetSource
import app.ultradev.hytaleuiparser.source.parsing.LangParser
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension

/**
 * A frozen view of an asset source that is used for validation
 */
class AssetIndex(
    val assetPaths: List<Path>,
    val translationKeys: Map<String, String>
) {
    companion object {
        fun buildIndex(source: AssetSource): AssetIndex {
            val allPaths = source.listAllFiles()

            val translationFiles = allPaths.filter { it.startsWith("Server/Languages/en-US/") && it.extension == "lang" }
            val translationKeys = mutableMapOf<String, String>()
            translationFiles.forEach {
                translationKeys.putAll(LangParser.parse(it.nameWithoutExtension, source.getAsset(it)!!))
            }

            return AssetIndex(allPaths, translationKeys)
        }
    }
}