package app.ultradev.hytaleuiparser.ast

class RootNode(
    override val children: List<AstNode>
) : AstNode() {
    init {
        children.forEach {
            if (it is NodeAssignReference || it is NodeAssignVariable || it is NodeElement) return@forEach
            error("Unexpected node in root: $it. Expected NodeAssignReference, NodeAssignVariable, or NodeElement.")
        }
    }

    val references: List<NodeAssignReference> get() = children.filterIsInstance<NodeAssignReference>()
    val variables: List<NodeAssignVariable> get() = children.filterIsInstance<NodeAssignVariable>()
    val elements: List<NodeElement> get() = children.filterIsInstance<NodeElement>()

    lateinit var variableValues: Map<String, AstNode>
}
