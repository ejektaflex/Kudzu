package io.ejekta.kudzu.value

import io.ejekta.kudzu.core.KudzuItem

open class KudzuLeaf<T>(var content: T) : KudzuItem {
    override fun toString(): String {
        return content.toString()
    }
}