
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.test.Category
import com.example.test.Product
import com.example.test.R
import com.inverce.mod.integrations.support.annotations.IBinder
import com.inverce.mod.integrations.support.recycler.MultiRecyclerAdapter

//package com.example.test
//
//import android.os.Bundle
//import android.support.annotation.CheckResult
//import android.support.annotation.IdRes
//import android.support.annotation.RestrictTo
//import android.support.v4.app.Fragment
//import android.support.v4.app.FragmentActivity
//import android.support.v4.app.FragmentManager
//import com.inverce.mod.core.IM
//import com.inverce.mod.core.Log
//import com.inverce.mod.core.R

class MyAdapter : MultiRecyclerAdapter<Any>() {
    init {
        register({ it is Product },{ Holder(it) }, R.layout.support_simple_spinner_dropdown_item)
        register({ it is Category }, { Holder2(it) }, R.layout.support_simple_spinner_dropdown_item)

        register( { true }, ::Holder, -1)
        register( { true }, ::onBindSample, ::Holder, R.layout.support_simple_spinner_dropdown_item)
    }

    fun onBindSample(holder: Holder, item: Any, position: Int) {

    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), IBinder<Product> {
        lateinit var tw: TextView
        override fun onBindViewHolder(item: Product, position: Int) {
            tw.text = item.name;
        }
    }

    private class Holder2(itemView: View) : RecyclerView.ViewHolder(itemView), IBinder<Category> {
        override fun onBindViewHolder(item: Category, position: Int) {

        }
    }
}
