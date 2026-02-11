package app.ultradev.hytaleuiparser.renderer

import java.awt.Graphics2D
import java.awt.image.BufferedImage

object NineSlice {
    fun scale(
        src: BufferedImage,

        targetWidth: Int,
        targetHeight: Int,

        borderLeft: Int,
        borderTop: Int,
        borderRight: Int,
        borderBottom: Int,
    ): BufferedImage {
        val out = BufferedImage(targetWidth, targetHeight, src.type)
        val g = out.createGraphics()

        // Defining the grid
        val sourceH = intArrayOf(
            0,
            borderLeft.coerceAtMost(src.width),
            (src.width - borderRight).coerceAtLeast(borderLeft).coerceAtMost(src.width),
            src.width
        )
        val sourceV = intArrayOf(
            0,
            borderTop.coerceAtMost(src.height),
            (src.height - borderBottom).coerceAtLeast(borderTop).coerceAtMost(src.height),
            src.height
        )

        val destH = intArrayOf(
            0,
            borderLeft.coerceAtMost(targetWidth),
            (targetWidth - borderRight).coerceAtLeast(borderLeft).coerceAtMost(targetWidth),
            targetWidth
        )
        val destV = intArrayOf(
            0,
            borderTop.coerceAtMost(targetHeight),
            (targetHeight - borderBottom).coerceAtLeast(borderTop).coerceAtMost(targetHeight),
            targetHeight
        )

        fun drawPiece(i: Int, j: Int) {
            drawSlice(
                g, src,
                sourceH[i], sourceV[j],
                sourceH[i+1], sourceV[j+1],
                destH[i], destV[j],
                destH[i+1], destV[j+1]
            )
        }

        for (i in 0..2) {
            for (j in 0..2) {
                drawPiece(i, j)
            }
        }
        g.dispose()

        return out
    }

    private fun drawSlice(
        g: Graphics2D,
        src: BufferedImage,
        sx1: Int,
        sy1: Int,
        sx2: Int,
        sy2: Int,
        dx1: Int,
        dy1: Int,
        dx2: Int,
        dy2: Int
    ) {
        val sw = sx2 - sx1
        val sh = sy2 - sy1
        val dw = dx2 - dx1
        val dh = dy2 - dy1
        if (sw <= 0 || sh <= 0 || dw <= 0 || dh <= 0) return
        g.drawImage(src, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null)
    }
}