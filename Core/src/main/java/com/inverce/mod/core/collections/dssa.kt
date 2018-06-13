package com.inverce.mod.core.collections

import java.util.*

class ds {

}

open class TreeNode<T : TreeNode<T>> @JvmOverloads constructor(children: List<T>? = null) {
    var children: List<T>? = null
        protected set

    init {
        if (children != null) {
            this.children = ArrayList(children)
        } else {
            this.children = ArrayList()
        }
    }

    @Synchronized
    fun inSize(): Int {
        var inSize = children!!.size
        for (child in children!!) {
            inSize += child.inSize()
        }
        return inSize
    }
}
