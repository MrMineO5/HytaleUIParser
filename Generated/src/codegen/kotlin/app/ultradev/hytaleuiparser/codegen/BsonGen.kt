package app.ultradev.hytaleuiparser.codegen

import app.ultradev.hytaleuiparser.validation.ElementType
import app.ultradev.hytaleuiparser.validation.types.TypeType
import com.squareup.kotlinpoet.*
import org.bson.*

object BsonGen {
    private const val PACKAGE: String = CodeGen.BASE_PACKAGE + ".bson"

    fun generateBsonExtensions() {
        val file = FileSpec.builder(PACKAGE, "extensions")

        generateTypeBsonExtensions(file)
        generateElementBsonExtensions(file)
        generateGenericElementBsonExtensions(file)

        file.build().writeTo(CodeGen.outputDir)
    }

    private fun generateTypeBsonExtensions(file: FileSpec.Builder) {
        TypeType.entries.forEach { type ->
            val parseBuilder = FunSpec.builder("parse${type.name}FromBson")
                .addParameter("bson", typeNameOf<BsonValue>())
                .returns(TypeGen.getTypeName(type))
            if (type.isStruct) {
                when (type) {
                    TypeType.PatchStyle -> {
                        parseBuilder.beginControlFlow("if (bson is %T) {", typeNameOf<BsonString>())
                        parseBuilder.beginControlFlow("return if (bson.value.startsWith(\"#\")) {")
                        parseBuilder.addStatement("%T(color = parseColorFromBson(bson))", TypeGen.getTypeName(type))
                        parseBuilder.nextControlFlow("else")
                        parseBuilder.addStatement("%T(texturePath = bson.value)", TypeGen.getTypeName(type))
                        parseBuilder.endControlFlow()
                        parseBuilder.endControlFlow()
                    }

                    else -> {}
                }

                parseBuilder.addStatement(
                    "require(bson is %T) { %P }",
                    typeNameOf<BsonDocument>(),
                    $$"Expected BsonDocument, got ${bson::class.simpleName}: $bson"
                )
                parseBuilder.addCode("return %T(\n", TypeGen.getTypeName(type))
                    .also {
                        type.allowedFields.forEach { (name, type) ->
                            it.addCode(
                                "    %L = bson[%S]?.let { parse${type.name}FromBson(it) },\n",
                                name.lowercaseFirstChar(),
                                name
                            )
                        }
                    }
                    .addCode(")")
            } else if (type.isEnum) {
                parseBuilder.addCode(
                    """
                    |require(bson is %T) { %P }
                    |return %T.valueOf(bson.value)
                    """.trimMargin(),
                    typeNameOf<BsonString>(),
                    $$"Expected BsonString, got ${bson::class.simpleName}: $bson",
                    TypeGen.getTypeName(type)
                )
            } else {
                when (type) {
                    TypeType.String -> parseBuilder.addCode(
                        """
                        |require(bson is %T) { %P }
                        |return bson.value
                        """.trimMargin(),
                        typeNameOf<BsonString>(),
                        "Expected BsonString, got ${'$'}{bson::class.simpleName}: ${'$'}bson"
                    )

                    TypeType.Boolean -> parseBuilder.addCode(
                        """
                        |require(bson is %T) { %P }
                        |return bson.value
                        """.trimMargin(),
                        typeNameOf<BsonBoolean>(),
                        "Expected BsonBoolean, got ${'$'}{bson::class.simpleName}: ${'$'}bson"
                    )

                    TypeType.Int32 -> parseBuilder.addCode(
                        """
                        |require(bson is %T) { %P }
                        |return bson.value
                        """.trimMargin(),
                        typeNameOf<BsonInt32>(),
                        "Expected BsonInt32, got ${'$'}{bson::class.simpleName}: ${'$'}bson"
                    )

                    TypeType.Float -> parseBuilder.addCode(
                        """
                        |require(bson is %T) { %P }
                        |return bson.value.toFloat()
                        """.trimMargin(),
                        typeNameOf<BsonDouble>(),
                        "Expected BsonDouble, got ${'$'}{bson::class.simpleName}: ${'$'}bson"
                    )

                    TypeType.Color -> parseBuilder.addCode(
                        """
                        |require(bson is %T) { %P }
                        |return bson.value.%M()
                        """.trimMargin(),
                        typeNameOf<BsonString>(),
                        "Expected BsonString, got ${'$'}{bson::class.simpleName}: ${'$'}bson",
                        MemberName("app.ultradev.hytaleuiparser.asttools", "parseHexColor")
                    )

                    TypeType.Dict -> parseBuilder.addCode(
                        """
                        |require(bson is %T) { %P }
                        |return bson.%M()
                        """.trimMargin(),
                        typeNameOf<BsonDocument>(),
                        "Expected BsonDocument, got ${'$'}{bson::class.simpleName}: ${'$'}bson",
                        MemberName("app.ultradev.hytaleuiparser.generated", "toDict")
                    )

                    else -> error("Unknown primitive type: $type")
                }
            }
            file.addFunction(parseBuilder.build())

            val setFieldBuilder = FunSpec.builder("setFieldBson")
                .receiver(TypeGen.getTypeName(type).copy(nullable = true))
                .addParameter("path", typeNameOf<List<String>>())
                .addParameter("bson", typeNameOf<BsonValue>())
                .addParameter(
                    ParameterSpec.builder("index", typeNameOf<Int>())
                        .defaultValue("0")
                        .build()
                )
                .returns(TypeGen.getTypeName(type))
            if (type.isStruct) {
                setFieldBuilder
                    .addStatement("if (index >= path.size) return parse${type.name}FromBson(bson)")
                    .addStatement("val value = this ?: %T.EMPTY", TypeGen.getTypeName(type))
                    .beginControlFlow("return when (path[index]) {")
                    .also {
                        type.allowedFields.forEach { (name, _) ->
                            it.addStatement(
                                "\"%L\" ->  value.copy(%L = value.%L.setFieldBson(path, bson, index + 1))",
                                name,
                                name.lowercaseFirstChar(),
                                name.lowercaseFirstChar()
                            )
                        }
                    }
                    .addStatement($$"else -> error(\"Invalid path: ${path[0]} in $${type.name}\")")
                    .endControlFlow()
            } else {
                setFieldBuilder
                    .addStatement(
                        "if (index < path.size) error(%S)",
                        "Cannot set field on non-struct type ${type.name}"
                    )
                    .addStatement("return parse${type.name}FromBson(bson)")
            }
            file.addFunction(setFieldBuilder.build())
        }
    }

    private fun generateElementBsonExtensions(file: FileSpec.Builder) {
        ElementType.entries.forEach { elementType ->
            file.addFunction(
                FunSpec.builder("setFieldBson")
                    .receiver(ElementGen.propertyClassName(elementType))
                    .addParameter("path", typeNameOf<List<String>>())
                    .addParameter("bson", typeNameOf<BsonValue>())
                    .addParameter(
                        ParameterSpec.builder("index", typeNameOf<Int>())
                            .defaultValue("0")
                            .build()
                    )
                    .returns(ElementGen.propertyClassName(elementType))

                    .addStatement("if (index >= path.size) error(\"Cannot set an element\")")
                    .beginControlFlow("return when (path[index]) {")
                    .also {
                        elementType.properties.forEach { (name, type) ->
                            it.addStatement(
                                "\"%L\" -> copy(%L = this.%L.setFieldBson(path, bson, index + 1))",
                                name,
                                name.lowercaseFirstChar(),
                                name.lowercaseFirstChar()
                            )
                        }
                    }
                    .addStatement($$"else -> error(\"Invalid path: ${path[0]} in $${elementType.name}\")")
                    .endControlFlow()
                    .build()
            )
        }
    }

    private fun generateGenericElementBsonExtensions(file: FileSpec.Builder) {
        file.addFunction(
            FunSpec.builder("setFieldBson")
                .receiver(ElementGen.propertiesInterface)
                .addParameter("path", typeNameOf<List<String>>())
                .addParameter("bson", typeNameOf<BsonValue>())
                .addParameter(
                    ParameterSpec.builder("index", typeNameOf<Int>())
                        .defaultValue("0")
                        .build()
                )
                .returns(ElementGen.propertiesInterface)
                .beginControlFlow("return when (this) {")
                .also {
                    ElementType.entries.forEach { elementType ->
                        it.addStatement(
                            "is %T -> this.setFieldBson(path, bson, index)",
                            ElementGen.propertyClassName(elementType)
                        )
                    }
                }
                .endControlFlow()
                .build()
        )
    }
}
