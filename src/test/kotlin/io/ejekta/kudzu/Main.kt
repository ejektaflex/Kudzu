package io.ejekta.kudzu

import kotlinx.serialization.json.*
import kotlinx.serialization.json.JsonObject

fun main() {

    val obj = buildJsonObject {
        put("k", "hello")
        putJsonObject("hai") {
            put("a", 1)
            put("b", 2)
        }
    }

    println(obj)

    fun strToJsonObject(str: String): JsonObject {
        return Json.decodeFromString(JsonElement.serializer(), str).jsonObject
    }

    val a = """{"a": 4, "b": null}"""

    val b = strToJsonObject(a)

    println(b)

    var v: KudzuVine

    val myObj = kudzu {
        leaf("a", 1)
        leaf("b", 4)
        vine("c") {
            leaf("name", "Bob")
            leaf("age", 30)
        }
        v = vine("d", "colors") {
            leaf("red", 0)
            leaf("orange", 1)
            leaf("yellow", 2)
        }
    }

    val myOtherObj = kudzu {
        vine("c") {
            leaf("weight", 150)
        }
        vine("d") {
            leaf("test")
        }
    }






    //println((trimmed as KudzuVine).keys)

    //val test = myOtherObj.vine("d", "colors").keys
    // println(test)





}

/*
public class MyBetterObject(private val content: Map<String, JsonElement>) : JsonElement(), Map<String, JsonElement> by content {
    public override fun equals(other: Any?): Boolean = content == other
    public override fun hashCode(): Int = content.hashCode()
    public override fun toString(): String {
        return content.entries.joinToString(
            separator = ",",
            prefix = "{",
            postfix = "}",
            transform = {(k, v) -> """"$k":$v"""}
        )
    }
}

 */