package com.inverce.mod.integrations.support.recycler;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.io.Serializable;

public class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    protected transient RecyclerView.LayoutManager mLayoutManager;
    protected transient LoadMore onLoadMore;
    // The minimum amount of items to have below your current scroll position

    protected State state;
    // True if we are still waiting for the last set of data to load.
    public boolean loading = true;
    // before loading more.
    public int visibleThreshold = 10;

    public EndlessRecyclerViewScrollListener(RecyclerView.LayoutManager layoutManager, LoadMore onLoadMore, int visibleThreshold) {
        this.mLayoutManager = layoutManager;
        state = new State();
        this.visibleThreshold = visibleThreshold;

        if (layoutManager instanceof GridLayoutManager) {
            this.visibleThreshold = visibleThreshold * ((GridLayoutManager) layoutManager).getSpanCount();
        }

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            this.visibleThreshold = visibleThreshold * ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }

        this.onLoadMore = onLoadMore;
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < state.previousTotalItemCount) {
            state.currentPage = state.startingPageIndex;
            state.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (this.loading && (totalItemCount > state.previousTotalItemCount)) {
            this.loading = false;
            state.previousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            state.currentPage++;
            onLoadMore.onLoadMore(state.currentPage, totalItemCount, view);
            loading = true;
        }
    }

    // Call this method whenever performing new searches
    public void resetState() {
        state.currentPage = state.startingPageIndex;
        state.previousTotalItemCount = 0;
        this.loading = true;
    }

    public static class State implements Serializable {
        // The current offset index of data you have loaded
        public int currentPage = 0;
        // The total number of items in the dataset after the last load
        public int previousTotalItemCount = 0;
        // Sets the starting page index
        public int startingPageIndex = 0;
    }

    public interface LoadMore {
        void onLoadMore(int page, int totalItemsCount, RecyclerView view);
    }
}