package com.inverce.mod.tests

import com.inverce.mod.core.collections.*
import org.junit.Assert.*
import org.junit.Test


class CollectionsTest {

    @Test
    fun sum_list_is_correct() {
        var l1 = listOf(1, 2, 3)
        var l2 = listOf(2, 3, 4)
        assertEquals(CollectionsEx.join(l1, l2)?.size?:0, 6)
    }

    @Test
    fun mapmaker_return_correct_hashmap() {
        val m = MapMaker
                .New("A", "B")
                .put("A", "C")
                .put("B", "D")
                .build()
        assertTrue(m.size == 2 && m["B"] == "D")
    }

    @Test
    fun mapmaker_with_keys_returns_map() {
        val m = MapMaker
                .keys("A", "B")
                .vals("C", "D")
                .build()
        assertTrue(m.size == 2 && m["B"] == "D")
    }

    @Test(expected = UnsupportedOperationException::class)
    fun readonly_list_does_not_allow_modifications() {
        val ro = MapToReadOnlyList<Int>(mutableListOf(1, 2, 3))
        assertEquals(ro[0], 1)
        assertEquals(ro.size, 3)
        ro[0] = 2 // should throw unsupported
    }

    @Test(expected = UnsupportedOperationException::class)
    fun list_equals_returns_true_when_list_the_same() {
        val l1 = listOf(1, 6, 21, 32, 2, 5)
        val l2 = listOf(1, 6, 21, 32, 2, 5)
        val l3 = listOf(2, 5, 22, 32, 2, 5)
        val l4 = listOf(2, 5, 22, 32, 2)

        assertTrue(CollectionsEx.equals(l1, l2))
        assertFalse(CollectionsEx.equals(l1, l3))
        assertFalse(CollectionsEx.equals(l1, l4))
    }

    class Node(val name: String, children: List<Node>? = null) : TreeNode<Node> (children) {
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
        val ttc_bfs_asc = TraverseTreeCollection(root, TraversalMethod.BFS or TraversalMethod.ASC)
                .toList()
        val ttc_dfs_desc = TraverseTreeCollection(root, TraversalMethod.DFS or TraversalMethod.DESC)
                .toList()

        val correct_bfs_asc = listOf(root, E, B, D, A, C)
        val correct_dfs_desc = listOf(root, E, D, C, B, A)


        assertTrue(CollectionsEx.equals(correct_bfs_asc, ttc_bfs_asc))
        assertTrue(CollectionsEx.equals(correct_dfs_desc, ttc_dfs_desc))
    }

}