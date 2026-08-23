package com.techfix.app.adapter;

/** Generic click callback used by the app's RecyclerView adapters. */
public interface OnItemClickListener<T> {
    void onItemClick(T item);
}
