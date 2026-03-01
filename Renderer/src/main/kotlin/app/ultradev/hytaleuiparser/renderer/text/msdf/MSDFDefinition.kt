package app.ultradev.hytaleuiparser.renderer.text.msdf

import kotlinx.serialization.Serializable

@Serializable
data class MSDFDefinition(
    val atlas: Atlas,
    val metrics: Metrics,
    val glyphs: List<Glyph>,
) {
    @Serializable
    data class Atlas(
        val type: String,
        val distanceRange: Int,
        val distanceRangeMiddle: Int,
        val size: Double,
        val width: Int,
        val height: Int,
        val yOrigin: String,
    )

    @Serializable
    data class Metrics(
        val emSize: Double,
        val lineHeight: Double,
        val ascender: Double,
        val descender: Double,
        val underlineY: Double,
        val underlineThickness: Double,
    )

    @Serializable
    data class Glyph(
        val unicode: Int,
        val advance: Double,
        val planeBounds: Bounds? = null,
        val atlasBounds: Bounds? = null,
    ) {
        @Serializable
        data class Bounds(
            val left: Double,
            val top: Double,
            val right: Double,
            val bottom: Double,
        )
    }
}
