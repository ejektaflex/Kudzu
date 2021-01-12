package io.ejekta.kudzu.ext

import io.ejekta.kudzu.core.KudzuVine
import io.ejekta.kudzu.value.KudzuLeaf
import kotlinx.serialization.json.*

// ### JsonObject -> Kudzu

fun JsonObject.toKudzu(): KudzuVine {
    return KudzuVine(toMap().map { (key, element) ->
        key to when (element) {
            is JsonNull -> KudzuLeaf.LeafNull
            is JsonObject -> element.toKudzu()
            is JsonPrimitive -> element.toKudzu()
            else -> throw Exception("Something else shows in JsonObject map when exporting!")
        }
    }.toMap().toMutableMap())
}

fun JsonPrimitive.toKudzu(): KudzuLeaf<*> {
    return when {
        isString -> KudzuLeaf.LeafString(content)
        intOrNull != null -> KudzuLeaf.LeafInt(int)
        else -> throw Exception("Can't parse JsonPrimitive type to Kudzu! It's raw content is: '$content'")
    }
}

// ### Kudzu -> JsonObject

fun KudzuVine.toJsonObject(): JsonObject {
    return JsonObject(content.map {
        it.key to when(val item = it.value) {
            is KudzuLeaf<*> -> item.toJsonElement()
            is KudzuVine -> item.toJsonObject()
            else -> throw Exception("Something else shows in KudzuVine map when exporting!")
        }
    }.toMap())
}

fun KudzuLeaf<*>.toJsonElement(): JsonElement {
    return when (this) {
        is KudzuLeaf.LeafInt -> JsonPrimitive(content)
        is KudzuLeaf.LeafNull -> JsonNull
        is KudzuLeaf.LeafString -> JsonPrimitive(content)
        else -> throw Exception("Could not turn KudzuLeaf into a JsonElement!")
    }
}
