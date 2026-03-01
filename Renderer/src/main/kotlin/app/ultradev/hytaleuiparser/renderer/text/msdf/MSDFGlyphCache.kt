package app.ultradev.hytaleuiparser.renderer.text.msdf

import java.util.LinkedHashMap
import kotlin.math.roundToInt

class MSDFGlyphCache(
    private val maxBytes: Long = 64L * 1024 * 1024, // 64 MiB default
) {
    private data class CacheKey(val ch: Char, val qSize: Int) {
        companion object {
            fun of(ch: Char, sizePx: Float): CacheKey {
                val q = (sizePx * 2f).roundToInt()
                return CacheKey(ch, q)
            }
        }
        val sizePx: Float get() = qSize / 2f
    }

    data class GlyphBitmap(
        val w: Int,
        val h: Int,
        val xOffset: Int,
        val yOffset: Int,
        val advance: Double,
        val alpha: ByteArray,
    ) {
        val bytes: Int get() = alpha.size

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as GlyphBitmap

            if (w != other.w) return false
            if (h != other.h) return false
            if (xOffset != other.xOffset) return false
            if (yOffset != other.yOffset) return false
            if (advance != other.advance) return false
            if (!alpha.contentEquals(other.alpha)) return false
            if (bytes != other.bytes) return false

            return true
        }

        override fun hashCode(): Int {
            var result = w
            result = 31 * result + h
            result = 31 * result + xOffset
            result = 31 * result + yOffset
            result = 31 * result + advance.hashCode()
            result = 31 * result + alpha.contentHashCode()
            result = 31 * result + bytes
            return result
        }
    }

    private val map = LinkedHashMap<CacheKey, GlyphBitmap>(256, 0.75f, true)
    private var usedBytes: Long = 0

    @Synchronized
    operator fun get(char: Char, size: Float): GlyphBitmap? = map[CacheKey.of(char, size)]

    @Synchronized
    fun getOrPut(char: Char, size: Float, compute: (char: Char, size: Float) -> GlyphBitmap): GlyphBitmap {
        val key = CacheKey.of(char, size)
        map[key]?.let { return it }

        val bmp = compute(char, size)
        require(bmp.alpha.size == bmp.w * bmp.h) { "alpha size must be w*h" }

        putInternal(key, bmp)
        return bmp
    }

    private fun putInternal(key: CacheKey, bmp: GlyphBitmap) {
        // If replacing existing entry, adjust usedBytes
        val old = map.put(key, bmp)
        if (old != null) usedBytes -= old.bytes.toLong()
        usedBytes += bmp.bytes.toLong()

        evictIfNeeded()
    }

    private fun evictIfNeeded() {
        if (usedBytes <= maxBytes) return
        val it = map.entries.iterator()
        while (usedBytes > maxBytes && it.hasNext()) {
            val e = it.next()
            usedBytes -= e.value.bytes.toLong()
            it.remove()
        }
    }

    @Synchronized
    fun clear() {
        map.clear()
        usedBytes = 0
    }

    @Synchronized
    fun stats(): String =
        "MSDFGlyphCache(entries=${map.size}, usedBytes=$usedBytes, maxBytes=$maxBytes)"
}