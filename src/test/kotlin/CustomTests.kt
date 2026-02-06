import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.Tokenizer
import app.ultradev.hytaleuiparser.Validator
import kotlin.test.Test

class CustomTests {
    private fun validateInternalFile(name: String) {
        val file = javaClass.getResourceAsStream("/$name.ui")!!.reader()
        val rootNode = Parser(Tokenizer(file)).finish()
        Validator(mapOf("$name.ui" to rootNode)).validate()
    }

    @Test
    fun testMissingVariables() {
        val file = javaClass.getResourceAsStream("/test-missing-variables.ui")!!.reader()
        val rootNode = Parser(Tokenizer(file)).finish()
        Validator(mapOf("test.ui" to rootNode), validateUnusedVariables = true).validate()

        println(rootNode)
    }

    @Test
    fun testSpreads() {
        validateInternalFile("test-spreads")
    }
}