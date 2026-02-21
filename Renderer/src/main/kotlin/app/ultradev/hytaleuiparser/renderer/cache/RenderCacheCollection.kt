package app.ultradev.hytaleuiparser.renderer.cache

import app.ultradev.hytaleuiparser.source.AssetSource

class RenderCacheCollection(source: AssetSource) {
    val images = ImageCache(source)
    val lang = LangCache(source)
}