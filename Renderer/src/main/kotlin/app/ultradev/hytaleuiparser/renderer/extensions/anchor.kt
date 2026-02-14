package app.ultradev.hytaleuiparser.renderer.extensions

import app.ultradev.hytaleuiparser.generated.types.Anchor

fun Anchor.leftFallback(): Int? = this.left ?: this.horizontal ?: this.full
fun Anchor.rightFallback(): Int? = this.right ?: this.horizontal ?: this.full
fun Anchor.topFallback(): Int? = this.top ?: this.vertical ?: this.full
fun Anchor.bottomFallback(): Int? = this.bottom ?: this.vertical ?: this.full

