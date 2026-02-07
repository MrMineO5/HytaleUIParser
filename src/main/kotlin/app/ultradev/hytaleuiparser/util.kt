package app.ultradev.hytaleuiparser

import app.ultradev.hytaleuiparser.ast.AstNode

fun List<AstNode>.clone() = map { it.clone() }
