package app.ultradev.hytaleuiparser.renderer

import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.layout.LayoutPass
import app.ultradev.hytaleuiparser.renderer.target.AWTRenderTarget
import java.awt.Graphics
import java.awt.Point
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import java.awt.image.BufferedImage
import javax.swing.JPanel

class HytaleUIPanel(
    val element: AbstractUIElement,
    val backgroundImage: BufferedImage? = null
) : JPanel() {
    val context = RenderContext()

    init {
        addMouseMotionListener(object : MouseMotionListener {
            override fun mouseDragged(e: MouseEvent) {
            }

            override fun mouseMoved(e: MouseEvent) {
                repaint()
            }
        })

        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent) {
                recomputeLayout()
            }
        })
    }

    fun recomputeLayout() {
        element.box = RenderBox(0, 0, width, height)
        if (element is BranchUIElement) LayoutPass.run(element)
        repaint()
    }

    override fun paintComponent(g: Graphics) {
        context.mousePosition = mousePosition ?: Point(0, 0)

        g.clearRect(0, 0, width, height)

        if (backgroundImage != null) {
            val backgroundWidth = width * backgroundImage.height / height
            val bgStartX = (backgroundImage.width - backgroundWidth) / 2
            val bgEndX = bgStartX + backgroundWidth
            g.drawImage(
                backgroundImage,
                0, 0,
                width, height,
                bgStartX, 0,
                bgEndX, backgroundImage.height,
                null
            )
        }

        val target = AWTRenderTarget(g)
        element.draw0(target, context)
    }
}