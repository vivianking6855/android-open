package com.open.appbase.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Base list adapter.
 *
 * @param <DT> the type parameter
 * @param <VH> the type parameter
 */
public abstract class BaseRecyclerListAdapter<DT, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    /**
     * The Data List
     */
    protected List<DT> mData;

    /**
     * Instantiates a new Base list adapter.
     */
    public BaseRecyclerListAdapter() {
    }

    /**
     * Sets data.
     *
     * @param list the list
     */
    public void setData(List<DT> list) {
        if (list == null) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }

        mData.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * Clear data.
     */
    public void clearData() {
        if (mData != null) {
            mData.clear();
            mData = null;
        }
    }

    /**
     * Add data.
     *
     * @param list the list
     */
    public void addData(List<DT> list) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

}
