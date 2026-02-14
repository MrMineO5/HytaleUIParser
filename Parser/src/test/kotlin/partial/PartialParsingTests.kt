package partial

import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.Tokenizer
import app.ultradev.hytaleuiparser.Validator
import app.ultradev.hytaleuiparser.ast.RootNode
import kotlin.test.Test

class PartialParsingTests {
    private fun testCommonsUI(): RootNode {
        val reader = TestUtils.getInternalFile("/test-commons.ui")
        return Parser(Tokenizer(reader)).finish()
    }
    private fun expectParserErrors(count: Int, path: String) {
        val reader = TestUtils.getInternalFile("/$path")
        val parser = Parser(Tokenizer(reader))
        val rootNode = parser.finish()
        val validator = Validator(
            mapOf(
                path to rootNode,
                "test-commons.ui" to testCommonsUI(),
            ),
            validateUnusedVariables = true
        )
        validator.validate()
        assert(parser.parserErrors.size == count) { "Expected $count errors, got ${parser.parserErrors.size}\n${parser.parserErrors.joinToString("\n") { it.message }}" }
    }

    @Test
    fun testMissingSemicolon() {
        expectParserErrors(1, "partial/test-missing-semicolon.ui")
    }

    @Test
    fun testMissingMemberIdentifier() {
        expectParserErrors(1, "partial/test-missing-member-identifier.ui")
    }

    @Test
    fun testMissingRefMemberVariable() {
        expectParserErrors(1, "partial/test-missing-ref-member-variable.ui")
    }
}