package app.ultradev.hytaleuiparser.renderer.cache

import app.ultradev.hytaleuiparser.source.AssetSourceProvider

abstract class RenderCache(private val assetSourceProvider: AssetSourceProvider) {
    protected val source get() = assetSourceProvider.assetSource

    abstract fun invalidate()
}