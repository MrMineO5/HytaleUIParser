package app.ultradev.hytaleuiparser.renderer.type

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)

    companion object {
        fun fromAwt(awtPoint: java.awt.Point) = Point(awtPoint.x, awtPoint.y)
    }
}
