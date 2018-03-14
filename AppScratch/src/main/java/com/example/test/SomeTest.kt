package com.example.test

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.inverce.mod.integrations.support.recycler.MultiRecyclerAdapter


fun variableTypes() {
    val value: Int
    print("hello")
    value = 0
//    value = 1; nie uda sie
}

data class Data(
        var isNew: Boolean,
        var id: Int
)

fun copyExample() {
    val d = Data(true, 32)
    val copy = d.copy(id = 33)
}

abstract class SampleAdapter<T>(var data: List<T> = ArrayList()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount() = data.size
    fun hasItems() = data.size > 0
}

data class Person(
        var id: Int,
        var name: String,
        val premiumAccountNo: String? = null
) {
    var accountNo: String
        get() = "Blank"
        set(value) {
            /** hahaha */
        }

    var phoneNo: String = ""
        private set
}

fun sendPremiumMagazines(no: String) {}
fun nullables() {
    val d: Person? = Person(1, "Jasio")
    // safe nullable
    if (d?.premiumAccountNo != null) {
        sendPremiumMagazines(d.premiumAccountNo)
    }

    d?.premiumAccountNo?.let {
        sendPremiumMagazines(it)
    }

    sendPremiumMagazines(d?.premiumAccountNo ?: "")

    // unsafe nullable
    sendPremiumMagazines(d?.premiumAccountNo!!)

    val list = listOf(3, 7, 12, null)
    var sum: Int = 0
    for (p in list) {
        sum += p ?: 0
    }
}

data class PointC(val x: Int, val y: Int)

fun standardKotlin() {
    var b = "String"

    b.let {

    }

    val args = Bundle().apply {
        putInt("32", 32)
    }

    val p = PointC(3, 5)

    // deconstructing
    val (x, y) = PointC(3, 5)


}

@JvmOverloads
fun funWithFunction(x: Int = 0, y: Int = 0, z: Int = 0) {
    
}

class StaticSample {
    companion object {
        @JvmStatic
        val VIEW_TYPE = 2;
    }
}

@Throws(NullPointerException::class)
fun thisMayThrow() {
    val ks : Int? =  null
    
    val keint = ks ?: throw NullPointerException()
}

fun funWithStreams() {
    val list = listOf(3, 4, 5, 6, 7, null)
    val sum = list.filterNotNull().sum()
    val bigger = list
            .filterNotNull()
            .filter { it > 3 }
            .map { it.toLong() }
    
}


open class TreeNode<T : TreeNode<T>> @JvmOverloads constructor(children: List<T>? = null) {
    var children: List<T>
        protected set

    init {
        if (children != null) {
            this.children = java.util.ArrayList(children)
        } else {
            this.children = java.util.ArrayList()
        }
    }

    @Synchronized
    fun inSize(): Int {
        var inSize = children.size
        for (child in children) {
            inSize += child.inSize()
        }
        return inSize
    }
    
    fun someSynchronizedBlock() {
        synchronized(this) {
            var inSize = children.size
            for (child in children) {
                inSize += child.inSize()
            }
        }
    }
}
open class ExData(isNew: Boolean, id: Int)
class SubData(isNew: Boolean, id: Int) : ExData(isNew, id)

class SomeAdapterItIs : MultiRecyclerAdapter<ExData>() {
    init {
        register({it is SubData}, ::bindSubData, ::VH1, R.layout.abc_action_menu_item_layout)
        register({it is ExData}, ::bindData, ::VH2, R.layout.abc_action_menu_item_layout)
    }

    fun bindSubData(vh: VH1, data: SubData, pos: @ParameterName(name = "position") Int) {

    }

    fun bindData(vh: VH2, data: ExData, pos: @ParameterName(name = "position") Int) {

    }

    class VH1(itemView: View?) : RecyclerView.ViewHolder(itemView)
    class VH2(itemView: View?) : RecyclerView.ViewHolder(itemView)
    
}