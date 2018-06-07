package com.open.appbase.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class BaseRecyclerArrayAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> items;

    public final T getItem(final int position) {
        return this.items.get(position);
    }

    @Override
    public int getItemCount() {
        return this.items != null ? this.items.size() : 0;
    }

    @Override
    public final void onBindViewHolder(final VH holder, final int position) {
        final T item = this.getItem(position);
        this.onBindViewHolder(holder, item);
    }

    public abstract void onBindViewHolder(final VH holder, final T item);
}
