import app.ultradev.hytaleuiparser.ast.AstNode
import java.io.Reader
import kotlin.reflect.KProperty

object TestUtils {
    fun checkAllPropertiesInitialized(nodes: Iterable<AstNode>) {
        fun checkLateinitProps(node: AstNode) {
            node::class.members
                .filterIsInstance<KProperty<*>>()
                .filter { it.isLateinit }
                .forEach {
                    try {
                        it.getter.call(node)
                    } catch(e: Exception) {
                        throw java.lang.RuntimeException(
                            "Lateinit property ${it.name} ${node.file.path}:${node.tokens.first().let { "${it.startLine+1}:${it.startColumn+1}" }} on ${node.getAstPath()} not initialized in ${node.file.path}",
                            e
                        )
                    }
                }
            node.children.forEach { checkLateinitProps(it) }
        }
        nodes.forEach { root ->
            checkLateinitProps(root)
        }
    }

    fun getInternalFile(path: String): Reader = javaClass.getResourceAsStream(path)!!.reader()
}