package app.ultradev.hytaleuiparser.codegen

import app.ultradev.hytaleuiparser.ast.VariableValue
import app.ultradev.hytaleuiparser.validation.types.TypeType
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.MemberName.Companion.member
import java.awt.Color

object TypeGen {
    private const val PACKAGE: String = CodeGen.BASE_PACKAGE + ".types"

    fun generateTypes() {
        TypeType.entries.forEach(::generateType)
    }

    private fun getTypeName(type: TypeType): ClassName {
        return if (type.isPrimitive) {
            when (type) {
                TypeType.String -> String::class.asTypeName()
                TypeType.Int32 -> Int::class.asTypeName()
                TypeType.Float -> Float::class.asTypeName()
                TypeType.Boolean -> Boolean::class.asTypeName()
                TypeType.Color -> Color::class.asTypeName()
                else -> error("Unknown primitive type: $type")
            }
        } else {
            ClassName(PACKAGE, type.name)
        }
    }

    private fun generateType(type: TypeType) {
        val spec = if (type.isEnum) {
            generateEnum(type)
        } else if (type.isStruct) {
            generateStruct(type)
        } else {
            return
        }
        FileSpec.get(PACKAGE, spec)
            .writeTo(CodeGen.outputDir)
    }

    private fun generateEnum(type: TypeType): TypeSpec {
        val enumClass = TypeSpec.enumBuilder(type.name)
        type.enum.forEach { enumClass.addEnumConstant(it) }

        val companion = TypeSpec.companionObjectBuilder()
        companion.addFunction(
            FunSpec.builder("fromVariable")
                .addParameter("variable", VariableValue::class)
                .returns(getTypeName(type))
                .addCode(
                    """
                    |return valueOf(variable.%M())
                """.trimMargin(),
                    MemberName("app.ultradev.hytaleuiparser.asttools", "valueAsEnum")
                )
                .build()
        )

        enumClass.addType(companion.build())

        return enumClass.build()
    }


    private fun convertProperty(name: String, type: TypeType): CodeBlock {
        if (type.isPrimitive) {
            // Special case for paths
            if (name == "TexturePath") {
                return CodeBlock.of(
                    "properties[\"%L\"]?.%M()",
                    name,
                    MemberName("app.ultradev.hytaleuiparser.asttools", "valueAsPath")
                )
            }

            return CodeBlock.of(
                "properties[\"%L\"]?.%M()",
                name,
                MemberName("app.ultradev.hytaleuiparser.asttools", "valueAs${type.name}")
            )
        } else {
            return CodeBlock.of(
                "properties[\"%L\"]?.let(%L)",
                name,
                getTypeName(type).member("fromVariable").reference()
            )
        }
    }

    private fun generateStruct(type: TypeType): TypeSpec {
        val structClass = TypeSpec.classBuilder(type.name)
            .addModifiers(KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .also {
                        type.allowedFields.forEach { (name, type) ->
                            it.addParameter(
                                ParameterSpec.builder(name.lowercaseFirstChar(), getTypeName(type).copy(nullable = true))
                                    .defaultValue("null")
                                    .build()
                            )
                        }
                    }
                    .build())
            .also {
                type.allowedFields.forEach { (name, type) ->
                    it.addProperty(
                        PropertySpec.builder(name.lowercaseFirstChar(), getTypeName(type).copy(nullable = true))
                            .initializer(name.lowercaseFirstChar())
                            .build()
                    )
                }
            }

        val companion = TypeSpec.companionObjectBuilder()
        companion.addFunction(
            FunSpec.builder("fromVariable")
                .addParameter("variable", VariableValue::class)
                .returns(getTypeName(type))
                .addStatement(
                    "return fromProperties(variable.%M())",
                    MemberName("app.ultradev.hytaleuiparser.asttools", "valueAsProperties")
                )
                .build()
        )
        companion.addFunction(
            FunSpec.builder("fromProperties")
                .addParameter("properties", typeNameOf<Map<String, VariableValue>>())
                .returns(getTypeName(type))
                .addCode("return %T(\n", getTypeName(type))
                .also {
                    type.allowedFields.forEach { (name, type) ->
                        it.addCode("    %L = %L,\n", name.lowercaseFirstChar(), convertProperty(name, type))
                    }
                }
                .addCode(")")
                .build()
        )
        companion.addProperty(
            PropertySpec.builder("EMPTY", getTypeName(type))
                .initializer("%T()", getTypeName(type))
                .build()
        )

        structClass.addType(companion.build())

        return structClass.build()
    }
}