package app.ultradev.hytaleuiparser.renderer.target.opengl.internal

import app.ultradev.hytaleuiparser.renderer.target.opengl.GLShader
import com.jogamp.opengl.GL
import com.jogamp.opengl.GL3
import java.nio.IntBuffer

class GLShaderManager {
    val shaders = mutableMapOf<GLShader, Int>()

    operator fun get(shader: GLShader) = shaders[shader]!!

    fun init(gl: GL3, quadVBO: Int) {
        GLShader.entries.forEach {
            shaders[it] = loadShader(gl, quadVBO, it.name.lowercase(), it.mainTex)
        }
    }

    fun dispose(gl: GL3) {
        shaders.values.forEach { gl.glDeleteProgram(it) }
    }

    private fun loadShader(gl: GL3, quadVBO: Int, name: String, textureIndex: Int = -1): Int {
        val shader = compileAndLink(gl, "/shader/vert.vsh", "/shader/frag_${name}.fsh")

        val aPosLoc = gl.glGetAttribLocation(shader, "aPos")
        val aUvLoc = gl.glGetAttribLocation(shader, "aUV")

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, quadVBO)
        gl.glEnableVertexAttribArray(aPosLoc)
        gl.glVertexAttribPointer(aPosLoc, 2, GL.GL_FLOAT, false, 0, 0)
        gl.glEnableVertexAttribArray(aUvLoc)
        gl.glVertexAttribPointer(aUvLoc, 2, GL.GL_FLOAT, false, 0, 0)

        if (textureIndex != -1) {
            gl.glUseProgram(shader)
            val uTexLoc = gl.glGetUniformLocation(shader, "uTex")
            gl.glUniform1i(uTexLoc, textureIndex)
        }

        return shader
    }

    private fun compileShader(gl: GL3, type: Int, cpPath: String): Int {
        val src = javaClass.getResourceAsStream(cpPath)!!.reader().readText()

        val shader = gl.glCreateShader(type)
        val lines = arrayOf(src)
        val lengths = intArrayOf(src.length)
        gl.glShaderSource(shader, 1, lines, lengths, 0)
        gl.glCompileShader(shader)

        val status = IntBuffer.allocate(1)
        gl.glGetShaderiv(shader, GL3.GL_COMPILE_STATUS, status)

        val logLen = IntArray(1)
        gl.glGetShaderiv(shader, GL3.GL_INFO_LOG_LENGTH, logLen, 0)
        val log = ByteArray(maxOf(1, logLen[0]))
        gl.glGetShaderInfoLog(shader, log.size, null, 0, log, 0)
        val msg = String(log).trim()

        if (status[0] == GL.GL_FALSE) {
            gl.glDeleteShader(shader)
            throw RuntimeException("Shader compile failed: $msg\n---SRC---\n$src")
        } else if (msg.isNotEmpty() && msg[0].code != 0) {
            println("Warning while compiling $cpPath:\n$msg")
        }
        return shader
    }


    private fun linkProgram(gl: GL3, vs: Int, fs: Int): Int {
        val prog = gl.glCreateProgram()
        gl.glAttachShader(prog, vs)
        gl.glAttachShader(prog, fs)
        gl.glLinkProgram(prog)

        val status = IntArray(1)
        gl.glGetProgramiv(prog, GL3.GL_LINK_STATUS, status, 0)
        if (status[0] == GL.GL_FALSE) {
            val logLen = IntArray(1)
            gl.glGetProgramiv(prog, GL3.GL_INFO_LOG_LENGTH, logLen, 0)
            val log = ByteArray(maxOf(1, logLen[0]))
            gl.glGetProgramInfoLog(prog, log.size, null, 0, log, 0)
            val msg = String(log).trim()
            gl.glDeleteProgram(prog)
            throw RuntimeException("Program link failed: $msg")
        }
        return prog
    }

    private fun compileAndLink(gl: GL3, vsPath: String, fsPath: String): Int {
        val vs = compileShader(gl, GL3.GL_VERTEX_SHADER, vsPath)
        val fs = compileShader(gl, GL3.GL_FRAGMENT_SHADER, fsPath)
        val program = linkProgram(gl, vs, fs)
        gl.glDeleteShader(vs)
        gl.glDeleteShader(fs)
        return program
    }
}