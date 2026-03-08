package app.ultradev.hytaleuiparser.renderer.target.opengl

import app.ultradev.hytaleuiparser.renderer.target.opengl.internal.GLConsts
import app.ultradev.hytaleuiparser.renderer.target.opengl.internal.GLImageManager
import app.ultradev.hytaleuiparser.renderer.target.opengl.internal.GLShaderManager
import com.jogamp.common.nio.Buffers
import com.jogamp.opengl.GL
import com.jogamp.opengl.GL3
import java.awt.Color
import java.awt.image.BufferedImage

class GLStateManager {
    lateinit var gl: GL3
        private set

    val imageManager: GLImageManager = GLImageManager()
    private val shaderManager: GLShaderManager = GLShaderManager()

    var quadVBO: Int = 0

    var activeShader: GLShader? = null
        private set

    fun setGL(gl: GL3) {
        this.gl = gl
        this.activeShader = null
    }

    fun init() {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        gl.glEnable(GL.GL_BLEND)
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA)

        quadVBO = createQuadVBO(gl)

        shaderManager.init(gl, quadVBO)
    }

    private fun createQuadVBO(gl: GL3): Int {
        val buffer = IntArray(1)
        gl.glGenBuffers(1, buffer, 0)
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer[0])
        gl.glBufferData(
            GL.GL_ARRAY_BUFFER,
            GLConsts.quadBuf.size * 4L,
            Buffers.newDirectFloatBuffer(GLConsts.quadBuf),
            GL.GL_STATIC_DRAW
        )
        return buffer[0]
    }


    fun dispose(gl: GL3) {
        gl.glDeleteBuffers(1, intArrayOf(quadVBO), 0)
        imageManager.dispose(gl)
        shaderManager.dispose(gl)
    }

    var width: Int = 0
    var height: Int = 0

    fun reshape(width: Int, height: Int) {
        this.width = width
        this.height = height

        shaderManager.shaders.values.forEach {
            gl.glUseProgram(it)
            val uViewportLoc = gl.glGetUniformLocation(it, "uViewport")
            gl.glUniform2f(uViewportLoc, width.toFloat(), height.toFloat())
        }
    }


    fun useShader(shader: GLShader) {
        if (activeShader == shader) return
        gl.glUseProgram(shaderManager[shader])
        activeShader = shader
    }

    fun getUniformLocation(name: String) = gl.glGetUniformLocation(shaderManager[activeShader!!], name)

    fun bindTexture(textureIndex: Int, image: BufferedImage) {
        gl.glActiveTexture(textureIndex)
        gl.glBindTexture(GL.GL_TEXTURE_2D, imageManager.getOrCreateTexture(gl, image))
    }


    fun setUniform1f(name: String, x: Float) {
        gl.glUniform1f(getUniformLocation(name), x)
    }

    fun setUniform4f(name: String, x: Float, y: Float, z: Float, w: Float) {
        gl.glUniform4f(getUniformLocation(name), x, y, z, w)
    }

    fun setUniformColor4f(name: String, color: Color) {
        setUniform4f(name, color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
    }
}