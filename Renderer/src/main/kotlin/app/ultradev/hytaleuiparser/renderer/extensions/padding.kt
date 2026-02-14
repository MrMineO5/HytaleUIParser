package app.ultradev.hytaleuiparser.renderer.extensions

import app.ultradev.hytaleuiparser.generated.types.Padding

fun Padding.leftFallback(): Int? = this.left ?: this.horizontal ?: this.full
fun Padding.rightFallback(): Int? = this.right ?: this.horizontal ?: this.full
fun Padding.topFallback(): Int? = this.top ?: this.vertical ?: this.full
fun Padding.bottomFallback(): Int? = this.bottom ?: this.vertical ?: this.full

