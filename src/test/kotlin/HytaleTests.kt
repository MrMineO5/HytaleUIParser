import app.ultradev.hytaleuiparser.Tokenizer
import java.io.File
import kotlin.test.Test

class HytaleTests {
    val hytaleAssetsDir = File(System.getenv("HYTALE_ASSETS"))

    @Test
    fun testCommon() {
        val file = hytaleAssetsDir.resolve("Common/UI/Custom/Common.ui")
        val tokenizer = Tokenizer(file.reader())

        while (true) {
            println(tokenizer.nextToken() ?: break)
        }
    }
}