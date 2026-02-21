package app.ultradev.hytaleuiparser.codegen

import java.io.File

fun main(args: Array<String>) {
    val outputDir = File(args[0])
    if (outputDir.exists())
        outputDir.deleteRecursively()
    outputDir.mkdirs()

    CodeGen.outputDir = outputDir

    TypeGen.generateTypes()
    ElementGen.generateElements()
    BsonGen.generateBsonExtensions()
}