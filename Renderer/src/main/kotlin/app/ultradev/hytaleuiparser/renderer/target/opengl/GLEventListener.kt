package app.ultradev.hytaleuiparser.renderer.target.opengl

import app.ultradev.hytaleuiparser.renderer.RenderBox
import app.ultradev.hytaleuiparser.renderer.RenderContext
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.layout.LayoutPass
import app.ultradev.hytaleuiparser.renderer.target.OpenGLRenderTarget
import app.ultradev.hytaleuiparser.renderer.text.msdf.MSDFRenderer
import app.ultradev.hytaleuiparser.source.AssetSource
import com.jogamp.opengl.GL
import com.jogamp.opengl.GL3
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLEventListener
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class GLEventListener(val rootUIElement: BranchUIElement, val source: AssetSource) : GLEventListener {
    val context = RenderContext(source)

    val stateManager = GLStateManager()

    override fun init(drawable: GLAutoDrawable) {
        stateManager.setGL(drawable.gl.gL3)
        stateManager.init()
    }

    override fun dispose(drawable: GLAutoDrawable) {
        stateManager.setGL(drawable.gl.gL3)
        stateManager.dispose(drawable.gl.gL3)
    }

    override fun reshape(
        drawable: GLAutoDrawable,
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ) {
        rootUIElement.box = RenderBox(x, y, width, height)
        LayoutPass.run(rootUIElement)

        stateManager.setGL(drawable.gl.gL3)
        stateManager.reshape(width, height)
    }

    val backgroundImage = ImageIO.read(javaClass.getResourceAsStream("/background.png"))
    override fun display(drawable: GLAutoDrawable) {
        stateManager.setGL(drawable.gl.gL3)

        val renderer = GLRenderer(stateManager)

        val windowWidthBg = stateManager.width * backgroundImage.height / stateManager.height
        val bgStartXBg = (backgroundImage.width - windowWidthBg) / 2f
        val bgStartXW = bgStartXBg * stateManager.height / backgroundImage.height
        renderer.drawImage(backgroundImage, -bgStartXW, 0f, stateManager.width + 2 * bgStartXW, stateManager.height.toFloat())

        val target = OpenGLRenderTarget(renderer)
        rootUIElement.draw0(target, context)
    }
}