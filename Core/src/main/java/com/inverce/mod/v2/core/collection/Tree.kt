@file:JvmName("Tree")

package com.inverce.mod.v2.core.collection

open class TreeNode<T : TreeNode<T>> @JvmOverloads constructor(var children: List<T> = emptyList()) {
    @Synchronized
    fun inSize(): Int {
        var inSize = children.size
        for (child in children) {
            inSize += child.inSize()
        }
        return inSize
    }
}

enum class TraversalMethod {
    BreadthFirstTraversal,
    DepthFirstTraversals,
}

open class TraverseTreeCollection<T : TreeNode<T>>
@JvmOverloads constructor(val node: T, val method: TraversalMethod = TraversalMethod.BreadthFirstTraversal) : kotlin.collections.AbstractCollection<T>() {
    override val size: Int get() = node.inSize()
    override fun iterator() = when (method) {
        TraversalMethod.BreadthFirstTraversal -> BFTIterator(node)
        else -> DFTIterator(node)
    }
}

open class BFTIterator<T : TreeNode<T>>(node: T) : TreeIterator<T>(node) {
    override fun pass(node: T) = node.apply { queue.addAll(children) }
}

open class DFTIterator<T : TreeNode<T>>(node: T) : TreeIterator<T>(node) {
    override fun pass(node: T) = node.apply { queue.addAll(0, children) }
}

abstract class TreeIterator<T : TreeNode<T>>(node: T) : kotlin.collections.Iterator<T> {
    val queue = java.util.LinkedList<T>(listOf(node))
    override fun hasNext() = !queue.isNotEmpty()
    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException()
        }
        return pass(queue.removeFirst())
    }
    abstract fun pass(node: T): T
}