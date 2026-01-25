import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.TokenIterator
import app.ultradev.hytaleuiparser.Tokenizer
import app.ultradev.hytaleuiparser.Validator
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
import kotlin.io.path.reader
import kotlin.io.path.relativeTo
import kotlin.io.path.walk
import kotlin.test.Test

class HytaleTests {
    val hytaleAssetsDir = File(System.getenv("HYTALE_ASSETS"))

    @Test
    fun testFile() {
        val dir = hytaleAssetsDir.resolve("Common/UI/Test")

        val testFile = dir.resolve("InGame/Hud/PortalPanel.ui")

        val tokenizer = Tokenizer(testFile.reader())
        tokenizer.forEach(::println)

//        dir.walk().forEach {
//            if (!it.isFile) return@forEach
//            if (it.extension != "ui") return@forEach
//
//            try {
//                val tokenizer = Tokenizer(it.reader())
//                val parser = Parser(TokenIterator(tokenizer))
//                val root = parser.finish()
//                Validator.validate(root)
//            } catch(e: Exception) {
//                throw RuntimeException("Failed to parse ${it.name}", e)
//            }
//        }
//
//        println(Validator.properties.entries.joinToString("\n") { "${it.key}: ${it.value}" })
//        println(Validator.types)
//
//        println()
//        println(Validator.typeProperties.entries.joinToString("\n") { "${it.key}: ${it.value}" })
    }
    
    @Test
    fun testCommon() {
        val dir = hytaleAssetsDir.resolve("Common/UI/Custom").toPath()

        val files = dir.walk().filter {
            it.isRegularFile() && it.extension == "ui"
        }.associate {
            val value = try {
                val tokenizer = Tokenizer(it.reader())
                val parser = Parser(tokenizer)
                parser.finish()
            } catch(e: Exception) {
                throw RuntimeException("Failed to parse ${it.name}", e)
            }
            it.relativeTo(dir).toString() to value
        }

        println("Parsed ${files.size} files")
//        println(files.entries.joinToString("\n") { "${it.key}: ${it.value}" })

        Validator(files).validate()
    }
}