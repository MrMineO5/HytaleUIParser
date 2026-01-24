import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.TokenIterator
import app.ultradev.hytaleuiparser.Tokenizer
import java.io.File
import kotlin.test.Test

class HytaleTests {
    val hytaleAssetsDir = File(System.getenv("HYTALE_ASSETS"))

    @Test
    fun testCommon() {
        val dir = hytaleAssetsDir.resolve("Common/UI/Custom")

        dir.walk().forEach {
            if (!it.isFile) return@forEach
            if (it.extension != "ui") return@forEach

            try {
                val tokenizer = Tokenizer(it.reader())
                val parser = Parser(TokenIterator(tokenizer))
                parser.finish()
            } catch(e: Exception) {
                throw RuntimeException("Failed to parse ${it.name}", e)
            }
        }
    }
}