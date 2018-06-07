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

/**
 * The type Recycler array adapter.
 * use it like
     recyclerView.setAdapter(new RecyclerArrayAdapter(this,
        getResources().getStringArray(R.array.algorithm_list)));
 *
 * @param <T> the type parameter
 */
public final class RecyclerArrayAdapter<T> extends BaseRecyclerArrayAdapter<T, RecyclerArrayAdapter.SimpleViewHolder> {
    private LayoutInflater mLayoutInflater;

    private int textViewId;
    private int layoutResource;

    /**
     * Instantiates a new Recycler array adapter.
     *
     * @param context the context
     * @param objects the objects
     */
    public RecyclerArrayAdapter(@NonNull Context context, @NonNull T[] objects) {
        this(context, android.R.layout.simple_list_item_1, android.R.id.text1, objects);
    }

    /**
     * Instantiates a new Recycler array adapter.
     *
     * @param context            the context
     * @param resource           the resource
     * @param textViewResourceId the text view resource id
     * @param objects            the objects
     */
    public RecyclerArrayAdapter(@NonNull Context context, @LayoutRes int resource,
                                @IdRes int textViewResourceId, @NonNull T[] objects) {
        mLayoutInflater = LayoutInflater.from(context);
        textViewId = textViewResourceId;
        layoutResource = resource;
        items = new ArrayList<>(Arrays.asList(objects));
    }

    @Override
    public void onBindViewHolder(RecyclerArrayAdapter.SimpleViewHolder holder, T item) {
        holder.tv.setText(item.toString());
    }

    @NonNull
    @Override
    public RecyclerArrayAdapter.SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(layoutResource, parent, false);
        return new SimpleViewHolder(view);
    }

    /**
     * The type Simple view holder.
     */
    class SimpleViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Tv.
         */
        TextView tv;

        /**
         * Instantiates a new Simple view holder.
         *
         * @param view the view
         */
        public SimpleViewHolder(View view) {
            super(view);
            tv = (TextView)view.findViewById(textViewId);
        }
    }

}
