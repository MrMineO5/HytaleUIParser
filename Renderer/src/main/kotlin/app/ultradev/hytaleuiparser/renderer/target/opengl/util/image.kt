package app.ultradev.hytaleuiparser.renderer.target.opengl.util

import com.jogamp.common.nio.Buffers
import java.awt.image.BufferedImage
import java.nio.ByteBuffer

fun bufferedImageToRGBABytes(img: BufferedImage): ByteBuffer {
    val w = img.width
    val h = img.height
    val pixels = IntArray(w * h)
    img.getRGB(0, 0, w, h, pixels, 0, w)

    val buf = Buffers.newDirectByteBuffer(w * h * 4)

    for (y in 0 until h) {
        for (x in 0 until w) {
            val argb = pixels[y * w + x]
            val a = (argb ushr 24) and 0xFF
            val r = (argb ushr 16) and 0xFF
            val g = (argb ushr 8) and 0xFF
            val b = (argb) and 0xFF
            buf.put(r.toByte())
            buf.put(g.toByte())
            buf.put(b.toByte())
            buf.put(a.toByte())
        }
    }
    buf.flip()
    return buf
}

fun bufferedImageToRGBBytes(img: BufferedImage): ByteBuffer {
    val w = img.width
    val h = img.height
    val pixels = IntArray(w * h)
    img.getRGB(0, 0, w, h, pixels, 0, w)

    val buf = Buffers.newDirectByteBuffer(w * h * 3)

    for (y in 0 until h) {
        for (x in 0 until w) {
            val argb = pixels[y * w + x]
            val r = (argb ushr 16) and 0xFF
            val g = (argb ushr 8) and 0xFF
            val b = (argb) and 0xFF
            buf.put(r.toByte())
            buf.put(g.toByte())
            buf.put(b.toByte())
        }
    }
    buf.flip()
    return buf
}
