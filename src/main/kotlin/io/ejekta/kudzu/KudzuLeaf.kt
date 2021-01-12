package io.ejekta.kudzu

sealed class KudzuLeaf<T>(var content: T) : KudzuItem {
    override fun toString(): String {
        return content.toString()
    }

    class LeafInt(num: Int) : KudzuLeaf<Int>(num)

    class LeafString(str: String) : KudzuLeaf<String>(str)

    object LeafNull : KudzuLeaf<Nothing?>(null)


}