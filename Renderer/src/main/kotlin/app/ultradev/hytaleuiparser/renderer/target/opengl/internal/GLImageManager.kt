package app.ultradev.hytaleuiparser.renderer.target.opengl.internal

import app.ultradev.hytaleuiparser.renderer.target.opengl.util.bufferedImageToRGBABytes
import app.ultradev.hytaleuiparser.renderer.target.opengl.util.bufferedImageToRGBBytes
import com.jogamp.opengl.GL
import com.jogamp.opengl.GL2
import com.jogamp.opengl.GL3
import java.awt.image.BufferedImage

class GLImageManager(val vramLimit: Int = 32*1024*1024) {
    data class LoadedImage(
        val image: BufferedImage,
        val gpuLoc: Int,
        var lastAccess: Long = System.currentTimeMillis(),
    ) {
        val bytes = image.width * image.height * 4
    }

    private val textures = mutableMapOf<BufferedImage, LoadedImage>()
    private var usedBytes = 0L

    fun createTextureRGBA(gl: GL3, img: BufferedImage): Int {
        val w = img.width
        val h = img.height
        val data = bufferedImageToRGBABytes(img)

        val ids = IntArray(1)
        gl.glGenTextures(1, ids, 0)
        val texId = ids[0]

        gl.glBindTexture(GL.GL_TEXTURE_2D, texId)

        // Sampling behavior
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR)
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR)
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE)
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE)

        // Upload to VRAM
        gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1)
        gl.glTexImage2D(
            GL.GL_TEXTURE_2D,
            0,
            GL.GL_RGBA,   // internal format
            w,
            h,
            0,
            GL.GL_RGBA,   // data format
            GL.GL_UNSIGNED_BYTE,
            data
        )

        gl.glBindTexture(GL.GL_TEXTURE_2D, 0)
        return texId
    }

    private fun createTextureRGB(gl: GL3, img: BufferedImage): Int {
        val w = img.width
        val h = img.height
        val data = bufferedImageToRGBBytes(img)

        val ids = IntArray(1)
        gl.glGenTextures(1, ids, 0)
        val texId = ids[0]

        gl.glBindTexture(GL.GL_TEXTURE_2D, texId)

        // Sampling behavior
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR)
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR)
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE)
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE)

        // Upload to VRAM
        gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1)
        gl.glTexImage2D(
            GL.GL_TEXTURE_2D,
            0,
            GL.GL_RGB,   // internal format
            w,
            h,
            0,
            GL.GL_RGB,   // data format
            GL.GL_UNSIGNED_BYTE,
            data
        )

        gl.glBindTexture(GL.GL_TEXTURE_2D, 0)
        return texId
    }

    fun getOrCreateTexture(gl: GL3, img: BufferedImage): Int {
        val existing = textures[img]
        if (existing != null) {
            existing.lastAccess = System.currentTimeMillis()
            return existing.gpuLoc
        }

        return putInternal(gl, img).gpuLoc
    }

    fun dispose(gl: GL3) {
        val glTexs = textures.values.map { it.gpuLoc }.toSet().toIntArray()
        gl.glDeleteTextures(glTexs.size, glTexs, 0)
        textures.clear()
    }

    private fun putInternal(gl: GL3, image: BufferedImage): LoadedImage {
        evictIfNeeded(gl, image.width * image.height * 4)

        val gpuLoc = when (image.type) {
            BufferedImage.TYPE_INT_ARGB,
            BufferedImage.TYPE_4BYTE_ABGR,
                -> createTextureRGBA(gl, image)

            BufferedImage.TYPE_INT_RGB,
            BufferedImage.TYPE_INT_BGR,
            BufferedImage.TYPE_3BYTE_BGR,
            BufferedImage.TYPE_BYTE_INDEXED,
                -> createTextureRGB(gl, image)

            else -> error("Unknown image type: ${image.type}")
        }

        val image = LoadedImage(image, gpuLoc)
        textures[image.image] = image
        usedBytes += image.bytes.toLong()

        return image
    }

    private fun evictIfNeeded(gl: GL3, additionalBytes: Int) {
        if (usedBytes + additionalBytes <= vramLimit) return
        while (usedBytes + additionalBytes > vramLimit) {
            val e = textures.values.minBy { it.lastAccess }
            textures.remove(e.image)
            gl.glDeleteTextures(1, intArrayOf(e.gpuLoc), 0)
            usedBytes -= e.bytes.toLong()
        }
    }
}
