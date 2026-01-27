import app.ultradev.hytaleuiparser.Parser
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
    val hytaleAssetsDir = Path(System.getenv("HYTALE_ASSETS"))

    @Test
    fun testClient() {
        val dir = hytaleAssetsDir.resolve("Common/UI/Test")

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

    @Test
    fun testFile() {
        val dir = hytaleAssetsDir.resolve("Common/UI/Test")

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

        //Validator(files).validateRoot("MainMenu/Adventure/WorldList.ui")
    }
    
    @Test
    fun testCommon() {
        val dir = hytaleAssetsDir.resolve("Common/UI/Custom")

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