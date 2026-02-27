import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.Tokenizer
import app.ultradev.hytaleuiparser.Validator
import kotlin.test.Test

class CustomTests {
    private fun validateInternalFile(name: String) {
        val file = javaClass.getResourceAsStream("/$name.ui")!!.reader()
        val parser = Parser(Tokenizer(file))
        val rootNode = parser.finish()
        val validator = Validator(mapOf("$name.ui" to rootNode), validateUnusedVariables = true)
        validator.validate()

        validator.validationErrors.forEach { println(it) }
        assert(validator.validationErrors.isEmpty()) { "Validation errors found" }
    }
    
    private fun parseInternalFile(name: String) {
        val file = javaClass.getResourceAsStream("/$name.ui")!!.reader()
        val parser = Parser(Tokenizer(file))
        val rootNode = parser.finish()

        parser.parserErrors.forEach { println(it) }
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

    @Test
    fun testStrings() {
        validateInternalFile("test-strings")
    }
    
    @Test
    fun testMissingVariable() {
        parseInternalFile("test-partial-parsing")
    }
}