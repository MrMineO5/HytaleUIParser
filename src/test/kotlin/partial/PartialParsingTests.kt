package partial

import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.Tokenizer
import app.ultradev.hytaleuiparser.Validator
import kotlin.test.Test

class PartialParsingTests {
    private fun expectParserErrors(count: Int, path: String) {
        val reader = TestUtils.getInternalFile(path)
        val parser = Parser(Tokenizer(reader))
        val rootNode = parser.finish()
        val validator = Validator(mapOf("test.ui" to rootNode), validateUnusedVariables = true)
        validator.validate()
        assert(parser.parserErrors.size == count)
        assert(validator.validationErrors.isEmpty())
    }

    @Test
    fun testMissingSemicolon() {
        expectParserErrors(1, "/partial/test-missing-semicolon.ui")
    }

    @Test
    fun testMissingMemberIdentifier() {
        expectParserErrors(1, "/partial/test-missing-member-identifier.ui")
    }
}