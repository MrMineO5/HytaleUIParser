import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.Tokenizer
import app.ultradev.hytaleuiparser.Validator
import kotlin.test.Test

class CustomTests {
    @Test
    fun testMissingVariables() {
        val file = javaClass.getResourceAsStream("/test-missing-variables.ui")!!.reader()
        val rootNode = Parser(Tokenizer(file)).finish()
        Validator(mapOf("test.ui" to rootNode), validateUnusedVariables = true).validate()

        println(rootNode)
    }
}