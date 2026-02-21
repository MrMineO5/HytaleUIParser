package app.ultradev.hytaleuiparser.codegen

import app.ultradev.hytaleuiparser.ast.VariableValue
import app.ultradev.hytaleuiparser.validation.ElementType
import app.ultradev.hytaleuiparser.validation.Elements
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.MemberName.Companion.member

object ElementGen {
    private const val PACKAGE: String = CodeGen.BASE_PACKAGE + ".elements"

    lateinit var propertiesInterface: ClassName

    fun propertyClassName(type: ElementType): ClassName {
        return ClassName(PACKAGE, type.name + "Properties")
    }

    fun generateElements() {
        propertiesInterface = ClassName(PACKAGE, "ElementProperties")
        val base = TypeSpec.interfaceBuilder(propertiesInterface)
            .addModifiers(KModifier.SEALED)
            .also {
                Elements.COMMON_PROPERTIES.forEach { (name, type) ->
                    it.addProperty(
                        PropertySpec.builder(name.lowercaseFirstChar(), TypeGen.getTypeName(type).copy(nullable = true))
                            .build()
                    )
                }
            }

        val companion = TypeSpec.companionObjectBuilder()
        companion.addFunction(
            FunSpec.builder("fromProperties")
                .addParameter("type", ElementType::class)
                .addParameter("properties", typeNameOf<Map<String, VariableValue>>())
                .returns(propertiesInterface)
                .beginControlFlow("return when (type)")
                .also {
                    ElementType.entries.forEach { type ->
                        it.addStatement("%M -> %T.fromProperties(properties)", type::class.member(type.name), propertyClassName(type))
                    }
                }
                .endControlFlow()
                .build()
        )
        base.addType(companion.build())

        FileSpec.get(PACKAGE, base.build())
            .writeTo(CodeGen.outputDir)

        ElementType.entries.forEach(::generateProperties)
    }

    private fun generateProperties(elementType: ElementType) {
        val propClass = generatePropertyStruct(elementType)
        FileSpec.get(PACKAGE, propClass)
            .writeTo(CodeGen.outputDir)
    }

    private fun generatePropertyStruct(type: ElementType): TypeSpec {
        val className = propertyClassName(type)

        val structClass = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.DATA)
            .addSuperinterface(propertiesInterface)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .also {
                        type.properties.forEach { (name, type) ->
                            it.addParameter(
                                ParameterSpec.builder(name.lowercaseFirstChar(), TypeGen.getTypeName(type).copy(nullable = true))
                                    .defaultValue("null")
                                    .build()
                            )
                        }
                    }
                    .build())
            .also {
                type.properties.forEach { (name, type) ->
                    it.addProperty(
                        PropertySpec.builder(name.lowercaseFirstChar(), TypeGen.getTypeName(type).copy(nullable = true))
                            .initializer(name.lowercaseFirstChar())
                            .also {
                                if (name in Elements.COMMON_PROPERTIES) it.addModifiers(KModifier.OVERRIDE)
                            }
                            .build()
                    )
                }
            }

        val companion = TypeSpec.companionObjectBuilder()
        companion.addFunction(
            FunSpec.builder("fromProperties")
                .addParameter("properties", typeNameOf<Map<String, VariableValue>>())
                .returns(className)
                .addCode("return %T(\n", className)
                .also {
                    type.properties.forEach { (name, type) ->
                        it.addCode("    %L = %L,\n", name.lowercaseFirstChar(), TypeGen.convertProperty(name, type))
                    }
                }
                .addCode(")")
                .build()
        )

        structClass.addType(companion.build())

        return structClass.build()
    }
}