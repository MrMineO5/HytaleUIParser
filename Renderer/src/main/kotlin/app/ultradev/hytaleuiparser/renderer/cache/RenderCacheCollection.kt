package app.ultradev.hytaleuiparser.renderer.cache

import app.ultradev.hytaleuiparser.renderer.cache.asset.ItemAsset
import app.ultradev.hytaleuiparser.renderer.cache.asset.ItemQualityAsset
import app.ultradev.hytaleuiparser.source.AssetSourceProvider

class RenderCacheCollection(source: AssetSourceProvider) {
    val images = ImageCache(source)
    val lang = LangCache(source)

    val items = JsonAssetCache<ItemAsset>(source, "Item/Items")
    val itemQualities = JsonAssetCache<ItemQualityAsset>(source, "Item/Qualities")

    fun invalidate() {
        images.invalidate()
        lang.invalidate()
    }
}