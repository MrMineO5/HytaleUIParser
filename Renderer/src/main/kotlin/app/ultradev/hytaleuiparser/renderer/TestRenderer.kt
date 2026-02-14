package app.ultradev.hytaleuiparser.renderer

import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.Tokenizer
import app.ultradev.hytaleuiparser.Validator
import app.ultradev.hytaleuiparser.ast.RootNode
import app.ultradev.hytaleuiparser.renderer.layout.LayoutPass
import app.ultradev.hytaleuiparser.renderer.target.AWTRenderTarget
import java.awt.Dimension
import java.awt.Graphics
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
        val frame = JFrame()

        val assets = parseServerAssets()

        val validator = Validator(assets)
        val testPage = validator.validateRoot("Pages/ItemRepairPage.ui") ?: error("Failed to validate page")

        val rootUIElement = UITransformer.transform(testPage)
        rootUIElement.box = RenderBox(50, 50, 1600, 800)
        LayoutPass.run(rootUIElement)

        frame.add(
            object : JPanel() {
                init {
                    preferredSize = Dimension(1800, 900)
                }

                override fun paintComponent(g: Graphics) {
                    val target = AWTRenderTarget(g)
                    rootUIElement.draw0(target)
//                    target.renderImage(image, 0, 0, 800, 600, 23, 23)
                }
            }
        )

        frame.pack()
        frame.isVisible = true
    }
}