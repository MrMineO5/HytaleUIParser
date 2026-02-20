package app.ultradev.hytaleuiparser.renderer

import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.layout.LayoutPass
import app.ultradev.hytaleuiparser.renderer.target.AWTRenderTarget
import app.ultradev.hytaleuiparser.source.AssetSource
import app.ultradev.hytaleuiparser.source.EmptyAssetSource
import java.awt.Graphics
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.event.MouseWheelEvent
import java.awt.image.BufferedImage
import javax.swing.JPanel

class HytaleUIPanel(
    val element: AbstractUIElement,
    val backgroundImage: BufferedImage? = null,
    val assetSource: AssetSource = EmptyAssetSource,
) : JPanel() {
    val context = RenderContext(assetSource)

    init {
        context.setCursor = ::setCursor

        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                context.mousePosition = e.point
                element.mouseMoved(context)
                context.previousMousePosition = context.mousePosition
                repaint()
            }
        })

        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                context.mousePosition = e.point
                element.mouseDown(context)
                repaint()
            }

            override fun mouseReleased(e: MouseEvent) {
                context.mousePosition = e.point
                element.mouseUp(context)
                repaint()
            }

            override fun mouseWheelMoved(e: MouseWheelEvent) {
                if (e.scrollType != MouseWheelEvent.WHEEL_UNIT_SCROLL) return
            }
        })

        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent) {
                recomputeLayout()
            }
        })

        // Ensure the layout is computed before the first paint
        recomputeLayout()
    }

    fun recomputeLayout() {
        element.box = RenderBox(0, 0, width, height)
        if (element is BranchUIElement) LayoutPass.run(element)
        repaint()
    }

    override fun paintComponent(g: Graphics) {
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