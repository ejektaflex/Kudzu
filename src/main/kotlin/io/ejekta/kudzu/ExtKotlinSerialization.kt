package io.ejekta.kudzu

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
        booleanOrNull != null -> KudzuLeaf.LeafBool(boolean)
        intOrNull != null -> KudzuLeaf.LeafInt(int)
        doubleOrNull != null -> KudzuLeaf.LeafDouble(double)
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
        is KudzuLeaf.LeafBool -> JsonPrimitive(content)
        is KudzuLeaf.LeafDouble -> JsonPrimitive(content)
    }
}
