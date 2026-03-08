package app.ultradev.hytaleuiparser.renderer.context

import app.ultradev.hytaleuiparser.renderer.cache.RenderCacheCollection
import app.ultradev.hytaleuiparser.source.AssetSourceProvider

class RenderContext(
    assetSource: AssetSourceProvider,
) {
    val cache: RenderCacheCollection = RenderCacheCollection(assetSource)

    val draw = DrawContext()
    val interactivity = InteractivityContext(draw)

    fun reset() {
        interactivity.reset()
    }

    fun invalidateCache() {
        cache.invalidate()
    }
}