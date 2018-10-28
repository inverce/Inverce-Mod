package com.inverce.mod.tests

import com.inverce.mod.v2.core.collection.TraversalMethod
import com.inverce.mod.v2.core.collection.TraverseTreeCollection
import com.inverce.mod.v2.core.collection.TreeNode
import com.inverce.mod.v2.core.collection.sameAs
import org.junit.Assert.assertTrue
import org.junit.Test


class CollectionsTest {

    class Node(val name: String, children: List<Node> = emptyList()) : TreeNode<Node>(children) {
        override fun toString(): String {
            return name
        }
    }

    @Test
    fun traverseTreeCollection_return_properlyOrderedElements() {
        val A = Node("A")
        val B = Node("B", listOf(A))
        val C = Node("C")
        val D = Node("D", listOf(C))
        val E = Node("E", listOf(D))
        val root = Node("ROOT", listOf(B, E));

//              R
//           B     E
//        A      D
//             C
        val ttc_bfs_asc = TraverseTreeCollection(root, TraversalMethod.BreadthFirstTraversal).toList()
        val ttc_dfs_desc = TraverseTreeCollection(root, TraversalMethod.DepthFirstTraversals).toList()

        val correct_bfs_asc = listOf(root, E, B, D, A, C)
        val correct_dfs_asc = listOf(root, E, D, C, B, A)


        assertTrue(correct_bfs_asc.sameAs(ttc_bfs_asc))
        assertTrue(correct_dfs_asc.sameAs(ttc_dfs_desc))
    }

}