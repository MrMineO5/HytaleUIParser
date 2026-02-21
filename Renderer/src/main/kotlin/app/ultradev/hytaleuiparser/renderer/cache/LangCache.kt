package app.ultradev.hytaleuiparser.renderer.cache

import app.ultradev.hytaleuiparser.source.AssetSource
import kotlin.io.path.Path

class LangCache(val source: AssetSource) {
    val translation = mutableMapOf<String, String>()

    operator fun get(translationKey: String): String {
        if (translationKey in translation) return translation[translationKey]!!
        val file = translationKey.substringBefore(".")
        val contents = source.getAsset(Path("Server/Languages/en-US/$file.lang"))?.bufferedReader()?.readText() ?: run {
            translation[translationKey] = translationKey
            return translationKey
        }
        translation.putAll(
            contents.lineSequence()
            .filter { it.isNotBlank() }
            .filter { it.contains("=") }
            .associate {
                "$file.${it.substringBefore("=").trim()}" to
                        it.substringAfter("=")
                            .trim().removePrefix("[TMP]")
                            .trim()
            })
        val res = translation[translationKey] ?: translationKey
        translation[translationKey] = res
        return res
    }
}