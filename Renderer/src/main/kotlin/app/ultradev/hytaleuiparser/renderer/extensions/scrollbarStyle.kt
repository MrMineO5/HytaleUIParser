package app.ultradev.hytaleuiparser.renderer.extensions

import app.ultradev.hytaleuiparser.generated.types.ScrollbarStyle

fun ScrollbarStyle.sizeFallback(): Int = this.size ?: 20
fun ScrollbarStyle.spacingFallback(): Int = this.spacing ?: 20
fun ScrollbarStyle.totalSpace(): Int = this.sizeFallback() + this.spacingFallback()
