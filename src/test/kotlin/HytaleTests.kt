import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.Tokenizer
import app.ultradev.hytaleuiparser.Validator
import app.ultradev.hytaleuiparser.ast.AstNode
import app.ultradev.hytaleuiparser.ast.NodeConstant
import app.ultradev.hytaleuiparser.ast.NodeIdentifier
import app.ultradev.hytaleuiparser.ast.NodeOpacity
import app.ultradev.hytaleuiparser.ast.NodeSelector
import app.ultradev.hytaleuiparser.ast.NodeToken
import kotlin.io.path.*
import kotlin.test.Test

class HytaleTests {
    val hytaleAssetsDir = Path(System.getenv("HYTALE_ASSETS"))

    private fun validatedServerAssets(): Validator {
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

        val validator = Validator(files)
        validator.validate()
        return validator
    }

    @Test
    fun testClient() {
        val dir = hytaleAssetsDir.resolve("Common/UI/Test")

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
    }

    @Test
    fun testFile() {
        val dir = hytaleAssetsDir.resolve("Common/UI/Test")

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

        //Validator(files).validateRoot("MainMenu/Adventure/WorldList.ui")
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
    }


    @Test
    fun testAllScopesResolved() {
        fun check(node: AstNode) {
            try {
                // TODO: It is up to debate whether these *should* have a scope, their scope isn't required for any type of validation, but might be more consistent to have one?
                if (node !is NodeToken &&
                    node !is NodeIdentifier &&
                    node !is NodeSelector &&
                    node !is NodeConstant &&
                    node !is NodeOpacity
                    ) node.resolvedScope
            } catch(e: UninitializedPropertyAccessException) {
                throw RuntimeException("Scope not initialized for ${node.javaClass.simpleName} ${node.text}")
            }
            node.children.forEach { check(it) }
        }

        val validator = validatedServerAssets()
        validator.files.values.forEach {
            check(it)
        }
    }
}