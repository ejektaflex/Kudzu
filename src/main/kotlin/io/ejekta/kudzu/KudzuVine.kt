package io.ejekta.kudzu

@KudzuMarker
class KudzuVine(
    val content: MutableMap<String, KudzuItem> = mutableMapOf()
) : KudzuItem, MutableMap<String, KudzuItem> by content {

    fun leaf(key: String, value: Int) {
        this[key] = KudzuLeaf.LeafInt(value)
    }

    fun leaf(key: String, value: String) {
        this[key] = KudzuLeaf.LeafString(value)
    }

    fun leaf(key: String, value: Nothing? = null) {
        this[key] = KudzuLeaf.LeafNull
    }

    fun trim(vararg keys: String) = trim(keys.toList())

    fun trim(keys: List<String>): KudzuItem {
        if (keys.isEmpty()) {
            throw Exception("KudzuVine::trim received an empty list of arguments!")
        }
        val targetVine = vine(keys.drop(1))
        return targetVine.remove(keys[0]) ?: throw Exception("Key did not exist in Vine!: ${keys[0]}")
    }

    fun vine(vararg keys: String) = vine(keys.toList())

    fun vine(vararg keys: String, vineFunc: KudzuVine.() -> Unit): KudzuVine {
        return vine(*keys).apply(vineFunc)
    }

    fun vine(keys: List<String>): KudzuVine {
        val key = keys.firstOrNull() ?: return this
        val oldVine = this[key] as? KudzuVine ?: KudzuVine()
        this[key] = oldVine
        return oldVine.vine(keys.drop(1))
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



}