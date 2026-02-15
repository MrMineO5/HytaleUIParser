package app.ultradev.hytaleuiparser.renderer

import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.Tokenizer
import app.ultradev.hytaleuiparser.Validator
import app.ultradev.hytaleuiparser.ast.RootNode
import app.ultradev.hytaleuiparser.renderer.layout.LayoutPass
import app.ultradev.hytaleuiparser.renderer.target.AWTRenderTarget
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Point
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
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
        val testPage = validator.validateRoot("Pages/LaunchPadSettingsPage.ui") ?: error("Failed to validate page")

        val rootUIElement = UITransformer.transform(testPage)
        rootUIElement.box = RenderBox(0, 0, 1600, 800)
        LayoutPass.run(rootUIElement)

        frame.add(
            object : JPanel() {
                init {
                    preferredSize = Dimension(1600, 800)

                    addMouseMotionListener(object : MouseMotionListener {
                        override fun mouseDragged(e: MouseEvent) {
                        }

                        override fun mouseMoved(e: MouseEvent) {
                            repaint()
                        }
                    })
                }

                override fun paintComponent(g: Graphics) {
                    g.clearRect(0, 0, width, height)
                    val target = AWTRenderTarget(g)
                    rootUIElement.draw0(target, mousePosition ?: Point(0, 0))
//                    target.renderImage(image, 0, 0, 800, 600, 23, 23)
                }
            }
        )

        frame.pack()
        frame.isVisible = true
    }
}