package app.ultradev.hytaleuiparser.renderer.text.msdf

import java.awt.*
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import kotlin.math.max
import kotlin.math.min

class MSDFRenderer(
    val atlas: BufferedImage,
    val definition: MSDFDefinition,
) {
    val lookupTable = definition.glyphs.associateBy { it.unicode.toChar() }
    val atlasWidth = atlas.width
    val red = IntArray(atlas.width * atlas.height)
    val green = IntArray(atlas.width * atlas.height)
    val blue = IntArray(atlas.width * atlas.height)

    init {
        val argb = IntArray(atlas.width * atlas.height)
        atlas.getRGB(0, 0, atlas.width, atlas.height, argb, 0, atlas.width)
        for (i in argb.indices) {
            val r = (argb[i] shr 16) and 0xFF
            val g = (argb[i] shr 8) and 0xFF
            val b = argb[i] and 0xFF
            red[i] = r
            green[i] = g
            blue[i] = b
        }
    }

    val cache = MSDFGlyphCache()
    val stringCache = MSDFStringCache()

    private fun sampleBilinearMsdf(
        bounds: MSDFDefinition.Glyph.Bounds,
        x: Double,
        y: Double,
    ): Double {
        val x = bounds.left + (bounds.right - bounds.left) * x
        val y = bounds.top + (bounds.bottom - bounds.top) * y

        val x1 = (x - 0.5).toInt()
        val y1 = (y - 0.5).toInt()
        val x2 = x1 + 1
        val y2 = y1 + 1
        val wx = x - x1 - 0.5
        val wy = y - y1 - 0.5

        val i11 = y1 * atlasWidth + x1
        val i21 = y1 * atlasWidth + x2
        val i12 = y2 * atlasWidth + x1
        val i22 = y2 * atlasWidth + x2

        val r =
            (red[i11] * (1 - wx) * (1 - wy) + red[i21] * wx * (1 - wy) + red[i12] * (1 - wx) * wy + red[i22] * wx * wy)
        val g =
            (green[i11] * (1 - wx) * (1 - wy) + green[i21] * wx * (1 - wy) + green[i12] * (1 - wx) * wy + green[i22] * wx * wy)
        val b =
            (blue[i11] * (1 - wx) * (1 - wy) + blue[i21] * wx * (1 - wy) + blue[i12] * (1 - wx) * wy + blue[i22] * wx * wy)

        return max(min(r, g), min(max(r, g), b)) / 255f
    }

    private fun createBitmap(ch: Char, size: Float): MSDFGlyphCache.GlyphBitmap {
        val glyph = lookupTable[ch]
            ?: return MSDFGlyphCache.GlyphBitmap(0, 0, 0, 0, 0.0, byteArrayOf())

        val scale = size / definition.metrics.emSize
        if (glyph.planeBounds == null || glyph.atlasBounds == null)
            return MSDFGlyphCache.GlyphBitmap(0, 0, 0, 0, glyph.advance * scale, byteArrayOf())

        val x0 = glyph.planeBounds.left * scale
        val y0 = glyph.planeBounds.top * scale
        val x1 = glyph.planeBounds.right * scale
        val y1 = glyph.planeBounds.bottom * scale
        val x0i = x0.toInt()
        val y0i = y0.toInt()
        val x1i = x1.toInt()
        val y1i = y1.toInt()
        val w = x1i - x0i
        val h = y1i - y0i

        val screenPxRange = size / definition.atlas.size * definition.atlas.distanceRange

        val out = ByteArray(w * h)
        for (y in y0i..<y1i) {
            for (x in x0i..<x1i) {
                val px = (x - x0) / (x1 - x0)
                val py = (y - y0) / (y1 - y0)
                val sd = sampleBilinearMsdf(glyph.atlasBounds, px, py)
                val screenPxDistance = screenPxRange * (sd - 0.5)
                val opacity = (screenPxDistance + 0.5).coerceIn(0.0, 1.0)
                out[(y - y0i) * w + x - x0i] = (opacity * 255).toInt().toByte()
            }
        }
        return MSDFGlyphCache.GlyphBitmap(w, h, x0i, y0i, glyph.advance * scale, out)
    }

    private fun blitGlyph(buf: IntArray, totalWidth: Int, bm: MSDFGlyphCache.GlyphBitmap, color: Int, x: Int, y: Int) {
        for (bx in 0..<bm.w) {
            for (by in 0..<bm.h) {
                val maskIndex = by * bm.w + bx
                val alpha = bm.alpha[maskIndex]
                if (alpha == 0.toByte()) continue

                val ox = x + bm.xOffset + bx
                if (ox < 0 || ox >= totalWidth) continue
                val oy = y + bm.yOffset + by
                val bufIndex = oy * totalWidth + ox
                if (oy < 0 || bufIndex >= buf.size) continue
                buf[bufIndex] = alphaBlend(dst = buf[bufIndex], colorARGB = color, alpha = alpha)
            }
        }
    }

    private fun alphaBlend(dst: Int, colorARGB: Int, alpha: Byte): Int {
        if (alpha == 0.toByte()) return dst
        if (alpha == 255.toByte()) return colorARGB or 0xff000000.toInt()

        val alpha = alpha.toInt() and 0xFF

        val oldR = ((dst shr 16) and 0xff)
        val oldG = ((dst shr 8) and 0xff)
        val oldB = (dst and 0xff)
        val colorR = ((colorARGB shr 16) and 0xff)
        val colorG = ((colorARGB shr 8) and 0xff)
        val colorB = (colorARGB and 0xff)

        val oldA = (dst shr 24) and 0xFF
        val newA = alpha + (oldA * (0xFF - alpha) ushr 8)
        val invNewA = 0xFF00 / newA

        val newR = (((oldR * alpha * (0xFF - oldA) ushr 8) + colorR * alpha) * invNewA) ushr 16
        val newG = (((oldG * alpha * (0xFF - oldA) ushr 8) + colorG * alpha) * invNewA) ushr 16
        val newB = (((oldB * alpha * (0xFF - oldA) ushr 8) + colorB * alpha) * invNewA) ushr 16

        return (newR shl 16) or (newG shl 8) or newB or (newA shl 24)
    }

    private fun renderString(text: String, size: Float, color: Int): MSDFStringCache.CachedString {
        val glyphs = text.map { cache.getOrPut(it, size, ::createBitmap) }

        var minX = Integer.MAX_VALUE
        var maxX = Integer.MIN_VALUE
        var minY = Integer.MAX_VALUE
        var maxY = Integer.MIN_VALUE
        var tx = 0.0
        for (glyph in glyphs) {
            minX = min(minX, glyph.xOffset + tx.toInt())
            maxX = max(maxX, glyph.xOffset + tx.toInt() + glyph.w)
            minY = min(minY, glyph.yOffset)
            maxY = max(maxY, glyph.yOffset + glyph.h)
            tx += glyph.advance
        }

        val outImg = BufferedImage(maxX - minX, maxY - minY, BufferedImage.TYPE_INT_ARGB)
        val outBuf = (outImg.raster.dataBuffer as DataBufferInt).data

        var dx = 0.0
        for (t in glyphs) {
            blitGlyph(outBuf, outImg.width, t, color, dx.toInt() - minX, -minY)
            dx += t.advance
        }

        return MSDFStringCache.CachedString(outImg, minX, minY)
    }

    fun drawString(g: Graphics, size: Float, x: Int, y: Int, text: String) {
        val color = g.color.rgb and 0x00ffffff
        val outImg = stringCache.getOrPut(text, size, color, ::renderString)
        g.drawImage(outImg.image, x + outImg.offsetX, y + outImg.offsetY, null)
    }


    fun getWidth(text: String, size: Float): Int {
        val scale = size / definition.metrics.emSize
        return text.asSequence().mapNotNull(lookupTable::get).sumOf { it.advance * scale }.toInt()
    }
    fun getHeight(size: Float): Int {
        val scale = size / definition.metrics.emSize
        return (definition.metrics.lineHeight * scale).toInt()
    }
    fun getAscent(size: Float): Int {
        val scale = size / definition.metrics.emSize
        return (definition.metrics.ascender * scale).toInt()
    }
    fun getDescent(size: Float): Int {
        val scale = size / definition.metrics.emSize
        return (definition.metrics.descender * scale).toInt()
    }
}
