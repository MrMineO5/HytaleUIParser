package app.ultradev.hytaleuiparser.generated

import org.bson.BsonArray
import org.bson.BsonBoolean
import org.bson.BsonDocument
import org.bson.BsonInt32
import org.bson.BsonInt64
import org.bson.BsonString
import org.bson.BsonValue

fun BsonDocument.toDict(): Map<String, Any?> {
    fun convert(value: BsonValue): Any? = when (value) {
        is BsonDocument -> value.toDict()
        is BsonArray -> value.map { convert(it) }
        is BsonString -> value.value
        is BsonInt32 -> value.value
        is BsonInt64 -> value.value
        is BsonBoolean -> value.value
        else -> null
    }

    return entries.associate { it.key to convert(it.value) }
}