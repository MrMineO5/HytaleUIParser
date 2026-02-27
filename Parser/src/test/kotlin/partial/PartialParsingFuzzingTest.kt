package partial

import TestUtils
import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.ParserException
import app.ultradev.hytaleuiparser.Tokenizer
import app.ultradev.hytaleuiparser.Validator
import app.ultradev.hytaleuiparser.token.Token
import kotlin.test.Test

class PartialParsingFuzzingTest {
    private fun tokenize(eofCount: Int): List<Token> {
        val reader = TestUtils.getInternalFile("partial/test-fuzzing.ui")
        val tokenizer = Tokenizer(reader)

        val output = mutableListOf<Token>()
        while (tokenizer.hasNext()) {
            output += tokenizer.next()
        }
        for (i in 0 until eofCount) {
            output += tokenizer.next()
        }
        return output
    }

    private fun testWithNDropped(tokens: List<Token>, toDrop: Int, test: (List<Token>) -> Unit) {
        if (toDrop == 0) {
            test(tokens)
            return
        }

        for (i in 0 until tokens.size - toDrop) {
            val remaining = tokens.subList(0, i) + tokens.subList(i + 1, tokens.size)
            testWithNDropped(remaining, toDrop - 1, test)
        }
    }


    data class TestState(
        var parseFailed: Int = 0,
        var errored: Int = 0,
        var success: Int = 0,
    )

    private var state = TestState()

    private fun doTest(tokens: List<Token>) {
        val parse = try {
            Parser(tokens.iterator()).finish()
        } catch (e: ParserException) {
//            println("Cannot yet partially parse: $e")
            state.parseFailed++
            return
        }

        try {
            Validator(mapOf("partial/test-fuzzing.ui" to parse)).validate()
            state.success++
        } catch(e: Exception) {
            e.printStackTrace()
            state.errored++
        }
    }

    @Test
    fun testOneMissing() {
        state = TestState()
        val tokens = tokenize(3)
        testWithNDropped(tokens, 1, ::doTest)

        println(state)
        assert(state.errored == 0) { "Expected no errors, got ${state.errored}" }
    }

    @Test
    fun testTwoMissing() {
        state = TestState()
        val tokens = tokenize(4)
        testWithNDropped(tokens, 2, ::doTest)

        println(state)
        assert(state.errored == 0) { "Expected no errors, got ${state.errored}" }
    }
}