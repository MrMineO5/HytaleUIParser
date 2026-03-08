package app.ultradev.hytaleuiparser.renderer.target

import app.ultradev.hytaleuiparser.renderer.type.RenderBox
import app.ultradev.hytaleuiparser.renderer.TestRenderer
import app.ultradev.hytaleuiparser.renderer.command.CommandApplicator
import app.ultradev.hytaleuiparser.renderer.render.RenderImage
import app.ultradev.hytaleuiparser.renderer.target.opengl.GLEventListener
import app.ultradev.hytaleuiparser.renderer.target.opengl.GLRenderer
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
import app.ultradev.hytaleuiparser.renderer.type.Point
import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.awt.GLCanvas
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.event.MouseWheelEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import kotlin.system.exitProcess

class OpenGLRenderTarget(val renderer: GLRenderer) : RenderTarget {
    override val windowBounds: RenderBox
        get() = RenderBox(0, 0, renderer.stateManager.width, renderer.stateManager.height)

    override fun renderImage(
        image: RenderImage,
        box: RenderBox,
        horizontalBorder: Int,
        verticalBorder: Int
    ) {
        if (box.isEmpty()) return
        renderer.drawImage(
            image.image,
            box.x.toFloat(), box.y.toFloat(),
            box.width.toFloat(), box.height.toFloat(),
            horizontalBorder.toFloat(), verticalBorder.toFloat(),
            horizontalBorder.toFloat(), verticalBorder.toFloat(),
            image.scale.toFloat()
        )
    }

    override fun renderFill(color: Color, box: RenderBox) {
        renderer.drawFill(box.x.toFloat(), box.y.toFloat(), box.width.toFloat(), box.height.toFloat(), color)
    }

    override fun renderText(text: String, box: RenderBox, info: TextRenderStyle) {
        var textToDraw = text
        if (info.uppercase) textToDraw = textToDraw.uppercase()
        val lines = if (info.wrap) {
            info.wrap(text, box.width)
        } else textToDraw.split("\n")

        val alignments = info.calculateAlignment(box, lines)
        lines.zip(alignments).forEach { (line, coord) ->
            renderer.drawString(info.msdfFont, coord.first, coord.second, info.fontSize, line, info.color)
        }
    }

    override fun setClip(box: RenderBox?) {
        renderer.setClipBox(box)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val rootUIElement = CommandApplicator(TestRenderer.source)(TestRenderer.testCommands)

            val canvas = GLCanvas(GLCapabilities(GLProfile.get(GLProfile.GL3)))
            val renderer = GLEventListener(rootUIElement, TestRenderer.source)
            canvas.addGLEventListener(renderer)
            canvas.addMouseMotionListener(object : MouseMotionAdapter() {
                override fun mouseMoved(e: MouseEvent) {
                    renderer.context.interactivity.updateMousePosition(Point.fromAwt(e.point))
                    rootUIElement.mouseMoved(renderer.context)
                    canvas.repaint()
                }

                override fun mouseDragged(e: MouseEvent) {
                    renderer.context.interactivity.updateMousePosition(Point.fromAwt(e.point))
                    rootUIElement.mouseMoved(renderer.context)
                    canvas.repaint()
                }
            })

            canvas.addMouseListener(object : MouseAdapter() {
                override fun mousePressed(e: MouseEvent) {
                    rootUIElement.mouseDown(renderer.context)
                    canvas.repaint()
                }

                override fun mouseReleased(e: MouseEvent) {
                    rootUIElement.mouseUp(renderer.context)
                    canvas.repaint()
                }
            })

            canvas.addMouseWheelListener { e ->
                if (e.scrollType != MouseWheelEvent.WHEEL_UNIT_SCROLL) return@addMouseWheelListener
                rootUIElement.mouseWheel(e.unitsToScroll, renderer.context)
                canvas.repaint()
            }

            val frame = JFrame("One Triangle AWT")
            frame.contentPane.add(canvas)
            frame.addWindowListener(object : WindowAdapter() {
                override fun windowClosing(windowevent: WindowEvent) {
                    frame.remove(canvas)
                    frame.dispose()
                    exitProcess(0)
                }
            })

            frame.setSize(640, 480)
            frame.isVisible = true
        }
    }
}