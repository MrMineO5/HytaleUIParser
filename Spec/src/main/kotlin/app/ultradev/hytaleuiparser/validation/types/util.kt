package app.ultradev.hytaleuiparser.validation.types

fun Iterable<TypeType>.unifyStruct(): Map<String, TypeType> =
    unifyStructOrNull() ?: throw IllegalArgumentException("unifyStruct must be called on structs only")

fun Iterable<TypeType>.unifyStructOrNull(): Map<String, TypeType>? {
    if (any { it.isEnum || it.isPrimitive }) return null

    val allowedProperties = first().allowedFields.toMutableMap()
    forEach { type ->
        val b = type.allowedFields
        allowedProperties.entries.removeIf { it.key !in b || it.value != b[it.key] }
    }
    return allowedProperties
}

fun Iterable<TypeType>.unifyEnum(): Set<String> =
    unifyEnumOrNull() ?: throw IllegalArgumentException("unifyEnum must be called on enums only")

fun Iterable<TypeType>.unifyEnumOrNull(): Set<String>? {
    if (any { !it.isEnum }) return null
    return map { it.enum }.reduce { a, b -> a.intersect(b) }
}

fun Iterable<TypeType>.displayName(): String = joinToString(" & ")
fun Iterable<TypeType>.displayFullStructure(): String {
    return "type ${displayName()} {\n" +
            unifyStruct().entries.joinToString("") {
                "    ${it.key}: ${it.value.name}\n"
            } +
            "}"
}
