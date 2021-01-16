package io.ejekta.kudzu

import java.lang.NullPointerException

@KudzuMarker
class KudzuVine(
    val content: MutableMap<String, KudzuItem> = mutableMapOf()
) : KudzuItem, MutableMap<String, KudzuItem> by content {

    @KudzuMarker
    operator fun invoke(func: KudzuVine.() -> Unit) = apply(func)

    fun clone(func: KudzuVine.() -> Unit = {}): KudzuVine {
        return KudzuVine(content.map {
            it.key to when(val item = it.value) {
                is KudzuVine -> item.clone()
                is KudzuLeaf<*> -> item.clone()
                else -> throw Exception("Cannot clone Kudzu item: $item")
            }
        }.toMap().toMutableMap()).apply(func)
        //return KudzuVine(content.toMutableMap()).apply(func)
    }


    fun leaf(key: String, value: Int) {
        this[key] = KudzuLeaf.LeafInt(value)
    }

    fun leaf(key: String, value: String) {
        this[key] = KudzuLeaf.LeafString(value)
    }

    fun leaf(key: String, value: Nothing? = null) {
        this[key] = KudzuLeaf.LeafNull
    }

    fun trim(vararg index: String) = trim(index.toList())

    fun trim(index: List<String>): KudzuItem {
        if (index.isEmpty()) {
            throw Exception("KudzuVine::trim received an empty list of arguments!")
        }
        val targetVine = query(index.drop(1))
        return targetVine.remove(index[0]) ?: throw Exception("Key did not exist in Vine!: ${index[0]}")
    }

    fun vine(vararg index: String) = vine(index.toList())

    fun vine(vararg index: String, vineFunc: KudzuVine.() -> Unit): KudzuVine {
        return vine(*index).apply(vineFunc)
    }

    fun vine(index: List<String>): KudzuVine {
        val key = index.firstOrNull() ?: return this
        val oldVine = this[key] as? KudzuVine ?: KudzuVine()
        this[key] = oldVine
        return oldVine.vine(index.drop(1))
    }

    fun query(vararg index: String) = query(index.toList())

    fun query(vararg index: String, queryFunc: KudzuVine.() -> Unit): KudzuVine {
        return query(*index).apply(queryFunc)
    }

    fun query(index: List<String>): KudzuVine {
        val key = index.firstOrNull() ?: return this
        val gotVine = this[key] as? KudzuVine ?: throw Exception("Key is not a vine!: $index")
        return gotVine.query(index.drop(1))
    }

    fun graft(other: KudzuVine) {
        for (entry in other) {
            when(val item = entry.value) {
                is KudzuLeaf<*> -> this[entry.key] = item
                is KudzuVine -> vine(entry.key).graft(item)
                // is KudzuLattice
                else -> throw Exception("Cannot graft ${entry.key} with value ${entry.value}")
            }
        }
    }

    fun growIsNull(vararg index: String): Boolean {
        val key = index.firstOrNull() ?: return false
        val root = query(index.dropLast(1).toList())
        val item = root[index.last()]!!.asLeaf()
        return item is KudzuLeaf.LeafNull
    }

    private fun <T : Any?> growLeafOrNull(keys: List<String>): T? {
        val item = query(keys.dropLast(1))[keys.last()]!!
        if (item is KudzuLeaf.LeafNull) return null
        val leaf = item as? KudzuLeaf<*>
            ?: throw Exception("$keys does not lead to a leaf!")
        return leaf.content as T?
    }

    private fun <T : Any> growLeaf(keys: List<String>): T =
        growLeafOrNull(keys) ?: throw NullPointerException("Leaf not found in vine! Path: $keys")

    fun growInt(vararg keys: String): Int = growInt(keys.toList())
    fun growInt(keys: List<String>): Int = growLeaf(keys)
    fun growIntOrNull(vararg keys: String): Int? = growIntOrNull(keys.toList())
    fun growIntOrNull(keys: List<String>): Int? = growLeafOrNull(keys)

    fun growString(vararg keys: String): String = growString(keys.toList())
    fun growString(keys: List<String>): String = growLeaf(keys)
    fun growStringOrNull(vararg keys: String): String? = growStringOrNull(keys.toList())
    fun growStringOrNull(keys: List<String>): String? = growLeafOrNull(keys)

    fun growBool(vararg keys: String): Boolean = growBool(keys.toList())
    fun growBool(keys: List<String>): Boolean = growLeaf(keys)
    fun growBoolOrNull(vararg keys: String): Boolean? = growBoolOrNull(keys.toList())
    fun growBoolOrNull(keys: List<String>): Boolean? = growLeafOrNull(keys)

    fun growDouble(vararg keys: String): Double = growDouble(keys.toList())
    fun growDouble(keys: List<String>): Double = growLeaf(keys)
    fun growDoubleOrNull(vararg keys: String): Double? = growDoubleOrNull(keys.toList())
    fun growDoubleOrNull(keys: List<String>): Double? = growLeafOrNull(keys)

}