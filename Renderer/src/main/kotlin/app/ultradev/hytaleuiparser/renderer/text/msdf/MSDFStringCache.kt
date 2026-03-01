package app.ultradev.hytaleuiparser.renderer.text.msdf

import java.awt.image.BufferedImage
import java.util.LinkedHashMap
import kotlin.math.roundToInt

class MSDFStringCache(
    private val maxBytes: Long = 64L * 1024 * 1024, // 64 MiB default
) {
    private data class CacheKey(val str: String, val qSize: Int, val color: Int) {
        companion object {
            fun of(str: String, sizePx: Float, color: Int): CacheKey {
                val q = (sizePx * 2f).roundToInt()
                return CacheKey(str, q, color)
            }
        }
        val sizePx: Float get() = qSize / 2f
    }

    data class CachedString(
        val image: BufferedImage,
        val offsetX: Int,
        val offsetY: Int,
    ) {
        val bytes: Int get() = image.width * image.height * 4
    }

    private val map = LinkedHashMap<CacheKey, CachedString>(256, 0.75f, true)
    private var usedBytes: Long = 0

    @Synchronized
    operator fun get(str: String, size: Float, color: Int): CachedString? = map[CacheKey.of(str, size, color)]

    @Synchronized
    fun getOrPut(str: String, size: Float, color: Int, compute: (str: String, size: Float, color: Int) -> CachedString): CachedString {
        val key = CacheKey.of(str, size, color)
        map[key]?.let { return it }

        val bmp = compute(str, size, color)
        putInternal(key, bmp)
        return bmp
    }

    private fun putInternal(key: CacheKey, image: CachedString) {
        // If replacing existing entry, adjust usedBytes
        val old = map.put(key, image)
        if (old != null) usedBytes -= old.bytes
        usedBytes += image.bytes

        evictIfNeeded()
    }

    private fun evictIfNeeded() {
        if (usedBytes <= maxBytes) return
        val it = map.entries.iterator()
        while (usedBytes > maxBytes && it.hasNext()) {
            val e = it.next()
            usedBytes -= e.value.bytes
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