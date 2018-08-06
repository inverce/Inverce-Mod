package com.inverce.mod.v2.integrations.recycler

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import java.io.Serializable

open class EndlessRecyclerViewScrollListener(
        @field:Transient protected var mLayoutManager: RecyclerView.LayoutManager,
        @field:Transient protected var onLoadMore: LoadMore, var visibleThreshold: Int = 10) : RecyclerView.OnScrollListener() {
    // The minimum amount of items to have below your current scroll position

    protected var state: State
    // True if we are still waiting for the last set of data to load.
    var loading = true

    init {
        state = State()
        visibleThreshold = when (mLayoutManager) {
            is GridLayoutManager -> visibleThreshold * (mLayoutManager as GridLayoutManager).spanCount
            is StaggeredGridLayoutManager -> visibleThreshold * (mLayoutManager as StaggeredGridLayoutManager).spanCount
            else -> visibleThreshold
        }
    }

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    override fun onScrolled(view: RecyclerView?, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount

        when (mLayoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> lastVisibleItemPosition = (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
            is LinearLayoutManager -> lastVisibleItemPosition = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < state.previousTotalItemCount) {
            state.currentPage = state.startingPageIndex
            state.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.loading = true
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (this.loading && totalItemCount > state.previousTotalItemCount) {
            this.loading = false
            state.previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            state.currentPage++
            onLoadMore.onLoadMore(state.currentPage, totalItemCount, view)
            loading = true
        }
    }

    // Call this method whenever performing new searches
    fun resetState() {
        state.currentPage = state.startingPageIndex
        state.previousTotalItemCount = 0
        this.loading = true
    }

    class State : Serializable {
        // The current offset index of data you have loaded
        var currentPage = 0
        // The total number of items in the dataset after the last load
        var previousTotalItemCount = 0
        // Sets the starting page index
        var startingPageIndex = 0
    }

    interface LoadMore {
        fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)
    }
}