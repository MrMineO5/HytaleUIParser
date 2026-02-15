package app.ultradev.hytaleuiparser.renderer

import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.Tokenizer
import app.ultradev.hytaleuiparser.Validator
import app.ultradev.hytaleuiparser.ast.RootNode
import java.awt.BorderLayout
import java.awt.Dimension
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.io.path.*

object TestRenderer {
    val basePath = Path(System.getenv("HYTALE_ASSETS")).resolve("Common/UI/Custom")

    private fun parseServerAssets(): Map<String, RootNode> {
        val dir = basePath

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

    @JvmStatic
    fun main(args: Array<String>) {
        val frame = JFrame("Hytale UI Renderer")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.size = Dimension(800, 600)

        val assets = parseServerAssets()

        val validator = Validator(assets)
        val testPage = validator.validateRoot("Pages/LaunchPadSettingsPage.ui") ?: error("Failed to validate page")

        val rootUIElement = UITransformer.transform(testPage)


        val backgroundImage = ImageIO.read(javaClass.getResourceAsStream("/background.png"))

        val parent = JPanel(BorderLayout())
        parent.add(HytaleUIPanel(rootUIElement, backgroundImage), BorderLayout.CENTER)

        frame.contentPane = parent

        frame.isVisible = true
    }
}