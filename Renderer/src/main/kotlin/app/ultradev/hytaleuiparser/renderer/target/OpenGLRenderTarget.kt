package app.ultradev.hytaleuiparser.renderer.target

import app.ultradev.hytaleuiparser.renderer.type.RenderBox
import app.ultradev.hytaleuiparser.renderer.TestRenderer
import app.ultradev.hytaleuiparser.renderer.command.CommandApplicator
import app.ultradev.hytaleuiparser.renderer.render.RenderImage
import app.ultradev.hytaleuiparser.renderer.target.opengl.GLEventListener
import app.ultradev.hytaleuiparser.renderer.target.opengl.GLRenderer
import app.ultradev.hytaleuiparser.renderer.text.TextRenderStyle
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
    override val box: RenderBox
        get() = RenderBox(0, 0, renderer.stateManager.width, renderer.stateManager.height)

    private var offsetX = 0
    private var offsetY = 0

    override fun renderImage(
        image: RenderImage,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        horizontalBorder: Int,
        verticalBorder: Int
    ) {
        if (width == 0 || height == 0) return
        renderer.drawImage(
            image.image,
            offsetX + x.toFloat(), offsetY + y.toFloat(),
            width.toFloat(), height.toFloat(),
            horizontalBorder.toFloat(), verticalBorder.toFloat(),
            horizontalBorder.toFloat(), verticalBorder.toFloat(),
            image.scale.toFloat()
        )
    }

    override fun renderFill(color: Color, x: Int, y: Int, width: Int, height: Int) {
        renderer.drawFill(offsetX + x.toFloat(), offsetY + y.toFloat(), width.toFloat(), height.toFloat(), color)
    }

    override fun renderText(text: String, box: RenderBox, info: TextRenderStyle) {
        var textToDraw = text
        if (info.uppercase) textToDraw = textToDraw.uppercase()
        val lines = if (info.wrap) {
            info.wrap(text, box.width)
        } else textToDraw.split("\n")

        val alignments = info.calculateAlignment(box.shift(offsetX, offsetY), lines)
        lines.zip(alignments).forEach { (line, coord) ->
            renderer.drawString(info.msdfFont, coord.first, coord.second, info.fontSize, line, info.color)
        }
    }

    private var clipBox: RenderBox? = null

    override fun setClip(box: RenderBox?): RenderBox? {
        val old = clipBox
        clipBox = box
        renderer.setClipBox(box)
        return old
    }

    override fun setOffset(x: Int, y: Int): Pair<Int, Int> {
        val oldX = offsetX
        val oldY = offsetY
        offsetX = x
        offsetY = y
        return oldX to oldY
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
                    renderer.context.mousePosition = e.point
                    rootUIElement.mouseMoved(renderer.context)
                    renderer.context.previousMousePosition = renderer.context.mousePosition
                    canvas.repaint()
                }
            })

            canvas.addMouseListener(object : MouseAdapter() {
                override fun mousePressed(e: MouseEvent) {
                    renderer.context.mousePosition = e.point
                    rootUIElement.mouseDown(renderer.context)
                    canvas.repaint()
                }

                override fun mouseReleased(e: MouseEvent) {
                    renderer.context.mousePosition = e.point
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