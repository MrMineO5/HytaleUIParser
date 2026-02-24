package app.ultradev.hytaleuiparser.renderer.command

import app.ultradev.hytaleuiparser.Parser
import app.ultradev.hytaleuiparser.Tokenizer
import app.ultradev.hytaleuiparser.Validator
import app.ultradev.hytaleuiparser.ast.FakeAstNode
import app.ultradev.hytaleuiparser.generated.bson.setFieldBson
import app.ultradev.hytaleuiparser.generated.elements.GroupProperties
import app.ultradev.hytaleuiparser.renderer.UITransformer
import app.ultradev.hytaleuiparser.renderer.cache.LangCache
import app.ultradev.hytaleuiparser.renderer.element.AbstractUIElement
import app.ultradev.hytaleuiparser.renderer.element.BranchUIElement
import app.ultradev.hytaleuiparser.renderer.element.impl.UIGroupElement
import app.ultradev.hytaleuiparser.renderer.extensions.insertBefore
import app.ultradev.hytaleuiparser.source.AssetSource
import app.ultradev.hytaleuiparser.source.AssetSources
import app.ultradev.hytaleuiparser.spec.command.Selector
import app.ultradev.hytaleuiparser.spec.command.UICommand
import org.bson.BsonDocument
import org.bson.BsonInt32
import org.bson.BsonString
import java.io.StringReader

class CommandApplicator(val assetSource: AssetSource) {
    val files = AssetSources.parseUIFiles(assetSource)
    val langCache = LangCache(assetSource) // TODO: We should probably handle translation in the render pipeline?

    init {
        Validator(files, assetSource = assetSource).validate()
    }

    operator fun invoke(commands: List<UICommand>): BranchUIElement = run(commands)

    fun run(commands: List<UICommand>): BranchUIElement {
        var output: BranchUIElement = UIGroupElement(
            FakeAstNode,
            emptyList(),
            GroupProperties()
        )
        for (command in commands) {
            output = apply(command, output)
        }
        return output
    }

    private fun getFile(name: String) = files["Common/UI/Custom/$name"]
        ?: error("Could not find file with name '$name'")

    fun apply(command: UICommand, element: BranchUIElement): BranchUIElement {
        val result = locate(element, command.selector)
            ?: error("Could not find element to apply command to: ${command.selectorString}")

        fun doReplacement(old: AbstractUIElement, new: AbstractUIElement): BranchUIElement {
            var oldElement = old
            var newElement = new

            var foundStartingPoint = old == result.element
            for (i in result.path.asReversed()) {
                if (foundStartingPoint) {
                    newElement = i.withChildren(i.children.map { if (it === oldElement) newElement else it })
                    oldElement = i
                } else {
                    foundStartingPoint = oldElement === i
                }
            }
            return newElement as BranchUIElement
        }

        return when (command.type) {
            UICommand.Type.Append -> {
                if (result.element !is BranchUIElement) error("Cannot append to non-branch element")

                val path = command.text ?: error("Append command requires a path in 'text'")
                val file = getFile(path)
                val uiRoot = UITransformer.transform(file)
                val unboxed = uiRoot.children

                doReplacement(result.element, result.element.withChildren(result.element.children + unboxed))
            }

            UICommand.Type.AppendInline -> {
                if (result.element !is BranchUIElement) error("Cannot append to non-branch element")
                val inlineText = command.text ?: error("AppendInline command requires inline text in 'text'")
                val adHocElement = parseAdHocElement(inlineText)
                val unboxed = adHocElement.children
                doReplacement(result.element, result.element.withChildren(result.element.children + unboxed))
            }

            UICommand.Type.InsertBefore -> {
                val path = command.text ?: error("Append command requires a path in 'text'")
                val file = getFile(path)
                val uiRoot = UITransformer.transform(file)
                val unboxed = uiRoot.children

                val parent = result.path.last()
                doReplacement(parent, parent.withChildren(parent.children.insertBefore(result.element, unboxed)))
            }

            UICommand.Type.InsertBeforeInline -> {
                val inlineText = command.text ?: error("AppendInline command requires inline text in 'text'")
                val adHocElement = parseAdHocElement(inlineText)
                val unboxed = adHocElement.children
                val parent = result.path.last()
                doReplacement(parent, parent.withChildren(parent.children.insertBefore(result.element, unboxed)))
            }

            UICommand.Type.Remove -> {
                val parent = result.path.last()
                doReplacement(parent, parent.withChildren(parent.children.filter { it != result.element }))
            }

            UICommand.Type.Set -> {
                val data = command.data ?: error("Set command requires data in 'data'")
                val bson = BsonDocument.parse(data)
                processBson(bson)
                val path = result.remainingSelector.firstPart() as? Selector.Field ?: error("Set command must be applied to a field")
                doReplacement(result.element, result.element.withProperties(
                    result.element.properties.setFieldBson(path.path, bson.getValue("0"))
                ))
            }

            UICommand.Type.Clear -> {
                if (result.element !is BranchUIElement) error("Cannot clear non-branch element")
                doReplacement(result.element, result.element.withChildren(emptyList()))
            }
        }
    }

    private fun parseAdHocElement(inlineText: String): UIGroupElement {
        val rootNode = Parser(Tokenizer(StringReader(inlineText))).finish()
        Validator(mapOf("__adhoc_element.ui" to rootNode), assetSource = assetSource).validate()
        return UITransformer.transform(rootNode)
    }


    data class LocatorResult(
        val element: AbstractUIElement,
        val path: List<BranchUIElement>,
        val remainingSelector: Selector
    )

    fun locate(
        startingPoint: BranchUIElement,
        selector: Selector,
        basePath: List<BranchUIElement> = emptyList()
    ): LocatorResult? {
        val first = selector.firstPart() ?: return LocatorResult(startingPoint, emptyList(), Selector.EMPTY)

        when (first) {
            is Selector.Named -> {
                for (child in startingPoint.children) {
                    if (child.selector == first.name) {
                        val newSelector = selector.pop()
                        val newFirst = newSelector.firstPart()
                        if (!selectsElement(newFirst)) return LocatorResult(child, basePath + startingPoint, newSelector)
                        if (child !is BranchUIElement) return null
                        return locate(child, newSelector, basePath + startingPoint)
                    } else {
                        if (child is BranchUIElement) {
                            val l = locate(child, selector, basePath + startingPoint)
                            if (l != null) return l
                        }
                    }
                }
                return null
            }

            is Selector.Index -> {
                val child = startingPoint.children.getOrNull(first.index) ?: return null
                val newSelector = selector.pop()
                val newFirst = newSelector.firstPart()
                if (!selectsElement(newFirst)) return LocatorResult(child, basePath + startingPoint, newSelector)
                if (child !is BranchUIElement) return null
                return locate(child, newSelector, basePath + startingPoint)
            }

            is Selector.Field -> error("Selector cannot start with a field")
        }
    }

    private fun selectsElement(part: Selector.Part?): Boolean = part is Selector.Named || part is Selector.Index


    private fun processBson(bson: BsonDocument) {
        bson.keys.forEach {
            val value = bson[it]
            if (value is BsonDocument) {
                if (value.contains("MessageId")) {
                    var message = langCache[value["MessageId"]!!.asString().value]
                    val params = value["Params"]?.asDocument() ?: BsonDocument()
                    params.keys.forEach { paramName ->
                        val param = params[paramName] ?: return@forEach
                        val str = when (param) {
                            is BsonInt32 -> {
                                message = message.replace(
                                    Regex("\\{$paramName, plural, one \\{(\\w+)} other \\{(\\w+)}}"),
                                    { match ->
                                        if (param.value == 1) {
                                            match.groupValues[1]
                                        } else {
                                            match.groupValues[2]
                                        }
                                    }
                                )
                                param.value.toString()
                            }
                            is BsonString -> param.value
                            else -> return@forEach
                        }
                        message = message.replace("{$paramName}", str)
                    }
                    bson[it] = BsonString(message)
                } else {
                    processBson(value)
                }
            }
        }
    }
}