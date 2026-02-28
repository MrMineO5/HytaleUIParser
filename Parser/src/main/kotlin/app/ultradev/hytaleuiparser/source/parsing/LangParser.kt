package app.ultradev.hytaleuiparser.source.parsing

import java.io.InputStream

object LangParser {
    fun parse(fileName: String, content: InputStream): Map<String, String> {
        return content.bufferedReader().lineSequence()
            .filter { it.isNotBlank() }
            .filter { it.contains("=") }
            .associate {
                "$fileName.${it.substringBefore("=").trim()}" to
                        it.substringAfter("=")
                            .trim().removePrefix("[TMP]")
                            .trim()
            }
            .toMap()
    }
}