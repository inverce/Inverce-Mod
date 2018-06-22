package com.inverce.mod.v2.integrations.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

typealias MultiPredicate<T> = (T) -> Boolean
typealias MultiBind<I, VH> = (VH, I, position: Int) -> Boolean
typealias MultiCreateVH<VH> = (parent: ViewGroup, inflater: LayoutInflater) -> VH
typealias MultiCreateVH2<VH> = (View) -> VH
