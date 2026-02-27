package app.ultradev.hytaleuiparser.renderer.cache

import app.ultradev.hytaleuiparser.source.AssetSourceProvider

class RenderCacheCollection(source: AssetSourceProvider) {
    val images = ImageCache(source)
    val lang = LangCache(source)

    fun invalidate() {
        images.invalidate()
        lang.invalidate()
    }
}