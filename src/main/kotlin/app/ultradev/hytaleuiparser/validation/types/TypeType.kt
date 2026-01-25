package app.ultradev.hytaleuiparser.validation.types

enum class TypeType(
    val isPrimitive: Boolean = true,
    val allowedFields: Map<String, TypeType>
) {
    String,
    Integer,
    Boolean,
    Float,
    Double,
    Anchor(
        mapOf(
            "Left" to Integer,
            "Right" to Integer,
            "Top" to Integer,
            "Bottom" to Integer,
            "Width" to Integer,
            "Height" to Integer,
            "MinWidth" to Integer,
            "MaxWidth" to Integer,
            "Full" to Integer,
            "Horizontal" to Integer,
            "Vertical" to Integer,
        )
    ),
    Padding(
        mapOf(
            "Left" to Integer,
            "Right" to Integer,
            "Top" to Integer,
            "Bottom" to Integer,
            "Horizontal" to Integer,
            "Vertical" to Integer,
            "Full" to Integer,
        )
    ),
    
    // Center, Left, Right, Start, End
    Alignment,
    LabelStyle(
        // TODO: move this to styles? Inherit from styles?
        mapOf(
            "FontSize" to Integer,
            "FontName" to String, // "Primary", "Secondary"
            "LetterSpacing" to Integer,
            "TextColor" to String,
            "RenderBold" to Boolean,
            "RenderUppercase" to Boolean,
            "RenderItalics" to Boolean,
            "HorizontalAlignment" to Alignment,
            "VerticalAlignment" to Alignment,
        )
    ),
    PatchStyle(
        mapOf(
            "TexturePath" to String,
            "Background" to String,
            "VerticalBorder" to Integer,
            "HorizontalBorder" to Integer,
            "Border" to Integer,
            "Color" to String, 
            "LabelStyle" to LabelStyle
        )
    ),
    ;

    constructor(allowedFields: Map<String, TypeType>) : this(false, allowedFields)
    constructor() : this(true, emptyMap())
}