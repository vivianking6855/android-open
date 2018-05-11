package com.open.appbase.adapter.recyclerview;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public final class RecyclerViewArrayAdapter<T> extends BaseRecyclerViewArrayAdapter<T, RecyclerViewArrayAdapter.SimpleViewHolder> {
    private LayoutInflater mLayoutInflater;

    private int textViewId;
    private int layoutResource;

    public RecyclerViewArrayAdapter(@NonNull Context context, @NonNull T[] objects) {
        this(context, android.R.layout.simple_list_item_1, android.R.id.text1, objects);
    }

    public RecyclerViewArrayAdapter(@NonNull Context context, @LayoutRes int resource,
                                    @IdRes int textViewResourceId, @NonNull T[] objects) {
        mLayoutInflater = LayoutInflater.from(context);
        textViewId = textViewResourceId;
        layoutResource = resource;
        items = new ArrayList<>(Arrays.asList(objects));
    }

    @Override
    public void onBindViewHolder(RecyclerViewArrayAdapter.SimpleViewHolder holder, T item) {
        holder.tv.setText(item.toString());
    }

    @NonNull
    @Override
    public RecyclerViewArrayAdapter.SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(layoutResource, parent, false);
        return new SimpleViewHolder(view);
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public SimpleViewHolder(View view) {
            super(view);
            tv = (TextView)view.findViewById(textViewId);
        }
    }

}
