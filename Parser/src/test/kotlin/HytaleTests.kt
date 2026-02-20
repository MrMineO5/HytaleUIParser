import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.Tokenizer
import app.ultradev.hytaleuiparser.Validator
import app.ultradev.hytaleuiparser.ast.RootNode
import app.ultradev.hytaleuiparser.source.ArchiveAssetSource
import app.ultradev.hytaleuiparser.source.AssetSource
import app.ultradev.hytaleuiparser.source.AssetSources
import java.io.StringReader
import kotlin.io.path.*
import kotlin.test.Test

class HytaleTests {
    val serverAssetSource: AssetSource

    init {
        val path = System.getenv("HYTALE_ASSETS")
        serverAssetSource = if (path != null) {
            ArchiveAssetSource(Path(path))
        } else {
            AssetSources.getAssetsZipSource()
        }
    }

    private fun parseServerAssets(): Map<String, RootNode> {
        return AssetSources.parseUIFiles(serverAssetSource)
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
        var count = 0
        validator.validationErrors.forEach {
            if (it.message.startsWith($$"No member @ButtonsDestructive on $Sounds")) return@forEach
            System.err.println(it)
            count++
        }
        assert(count == 0) { "Validation errors found" }
    }


    @Test
    fun testTokenizerOutputsValidTokens() {
        val assetSource = AssetSources.getAssetsZipSource()

        assetSource.listUIFiles().forEach {
            val text = assetSource.getAsset(it)!!.reader().readText()
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
        TestUtils.checkAllPropertiesInitialized(validator.files.values)
    }
}