import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.TokenIterator
import app.ultradev.hytaleuiparser.Tokenizer
import java.io.File
import kotlin.test.Test

class HytaleTests {
    val hytaleAssetsDir = File(System.getenv("HYTALE_ASSETS"))

    @Test
    fun testCommon() {
        val file = hytaleAssetsDir.resolve("Common/UI/Custom/Common.ui")
        val tokenizer = Tokenizer(file.reader())

        val tokens = tokenizer.asSequence().toList()
        println(tokens.joinToString("\n"))

        val parser = Parser(TokenIterator(tokens.iterator()))
        val root = parser.finish()

        root.children.forEach { println(it) }
    }
}