package com.inverce.mod.v2.integrations.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

typealias MultiBind<I, V> = (view: V, item: I, position: Int) -> Unit
typealias MultiCreateVH<V> = (parent: ViewGroup, inflater: LayoutInflater) -> V
typealias MultiViewToVH<VH> = (View) -> VH
typealias MultiPredicate<T> = (T) -> Boolean

interface MultiBinder<I> { // used in vh for bindings
    fun onBindViewHolder(item: I, position: Int): Boolean
}
