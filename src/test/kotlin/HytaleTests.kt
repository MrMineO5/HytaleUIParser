import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.Tokenizer
import app.ultradev.hytaleuiparser.Validator
import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.ast.NodeConstant
import app.ultradev.hytaleuiparser.ast.NodeIdentifier
import app.ultradev.hytaleuiparser.ast.NodeOpacity
import app.ultradev.hytaleuiparser.ast.NodeSelector
import app.ultradev.hytaleuiparser.ast.NodeToken
import app.ultradev.hytaleuiparser.ast.RootNode
import java.io.StringReader
import kotlin.io.path.*
import kotlin.reflect.KProperty
import kotlin.test.Test

class HytaleTests {
    val hytaleAssetsDir = Path(System.getenv("HYTALE_ASSETS"))

    private fun parseServerAssets(): Map<String, RootNode> {
        val dir = hytaleAssetsDir.resolve("Common/UI/Custom")

        return dir.walk().filter {
            it.isRegularFile() && it.extension == "ui"
        }.associate {
            val value = try {
                val tokenizer = Tokenizer(it.reader())
                val parser = Parser(tokenizer)
                parser.finish()
            } catch (e: Exception) {
                throw RuntimeException("Failed to parse ${it.name}", e)
            }
            it.relativeTo(dir).toString() to value
        }
    }

    @Test
    fun testServerValidate() {
        val files = parseServerAssets()
        val validator = Validator(files)
        validator.validate()
        validator.validationErrors.forEach {
            System.err.println(it)
        }
        assert(validator.validationErrors.isEmpty()) { "Validation errors found" }
    }

    @Test
    fun testServerValidateVariables() {
        val files = parseServerAssets()
        val validator = Validator(files, validateUnusedVariables = true)
        validator.validate()
        validator.validationErrors.forEach {
            System.err.println(it)
        }
        assert(validator.validationErrors.isEmpty()) { "Validation errors found" }
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
            } catch (e: Exception) {
                throw RuntimeException("Failed to parse ${it.name}", e)
            }
            it.relativeTo(dir).toString() to value
        }

        println("Parsed ${files.size} files")
//        println(files.entries.joinToString("\n") { "${it.key}: ${it.value}" })

        Validator(files).validate()
        val validator = Validator(files)
        validator.validate()
        validator.validationErrors.forEach { throw RuntimeException(it.toString()) }
    }


    @Test
    fun testTokenizerOutputsValidTokens() {
        val dir = hytaleAssetsDir.resolve("Common/UI/Custom")

        dir.walk().filter {
            it.isRegularFile() && it.extension == "ui"
        }.forEach {
            val text = it.readText()
            val tokenizer = Tokenizer(StringReader(text))
            val allTokens = tokenizer.asSequence().toList()

            for ((prev, curr) in allTokens.zipWithNext()) {
                if (prev.startOffset + prev.text.length != curr.startOffset) error("Tokens overlap or contain gaps: $prev, $curr")
            }
            assert(allTokens.joinToString("") { it.text } == text) { "Tokenizer output does not match input for ${it.name}" }
        }
    }

    @Test
    fun testAllLateinitPropertiesInitialized() {
        val files = parseServerAssets()
        val validator = Validator(files, validateUnusedVariables = true)
        validator.validate()
        assert(validator.validationErrors.isEmpty()) { "Validation errors found" }

        fun checkLateinitProps(node: AstNode) {
            node::class.members
                .filterIsInstance<KProperty<*>>()
                .filter { it.isLateinit }
                .forEach {
                    try {
                        it.getter.call(node)
                    } catch(e: Exception) {
                        throw java.lang.RuntimeException(
                            "Lateinit property ${it.name} on ${node.getAstPath()} not initialized in ${node.file.path}",
                            e
                        )
                    }
                }
            node.children.forEach { checkLateinitProps(it) }
        }
        validator.files.values.forEach { root ->
            checkLateinitProps(root)
        }
    }
}