package io.ejekta.kudzu

class KudzuLattice(
    val content: MutableList<KudzuItem> = mutableListOf()
) : KudzuItem, MutableList<KudzuItem> by content {

    fun slat(item: String?) = add(if (item != null) KudzuLeaf.LeafString(item) else KudzuLeaf.LeafNull)
    fun slat(item: Double?) = add(if (item != null) KudzuLeaf.LeafDouble(item) else KudzuLeaf.LeafNull)
    fun slat(item: Int?) = add(if (item != null) KudzuLeaf.LeafInt(item) else KudzuLeaf.LeafNull)
    fun slat(item: Boolean?) = add(if (item != null) KudzuLeaf.LeafBool(item) else KudzuLeaf.LeafNull)
    fun slat(item: Nothing? = null) = add(KudzuLeaf.LeafNull)

    fun slat(func: KudzuVine.() -> Unit = {}) {
        val item = KudzuVine()
        add(item.apply(func))
    }


}