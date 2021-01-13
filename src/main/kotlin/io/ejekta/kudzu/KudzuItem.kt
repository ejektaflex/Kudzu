package io.ejekta.kudzu

interface KudzuItem {
    fun isVine() = this is KudzuVine
    fun isLeaf() = this is KudzuLeaf<*>
    fun asVine() = this as KudzuVine
    fun asLeaf() = this as KudzuLeaf<*>
    fun asVineOrNull() = this as? KudzuVine
    fun asLeafOrNull() = this as? KudzuLeaf<*>
}