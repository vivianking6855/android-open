package com.wenxi.learn.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.wenxi.learn.demo.R;
import com.wenxi.learn.demo.model.HomeModel;

/**
 * Created by vivian on 2017/9/21.
 * recycler view third party template: https://github.com/captain-miao/RecyclerViewUtils
 */

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.RecyclerViewHolder> {

    private Context mContext;
    private HomeModel mData;

    public HomeRecyclerAdapter(Context context) {
        mContext = context;
    }

    public void setData(HomeModel model) {
        mData = model;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.home_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.hint.setText(mData.titleArray.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.titleArray.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView hint;

        RecyclerViewHolder(View view) {
            super(view);
            hint = (TextView) view.findViewById(R.id.tv_content);
        }
    }
}
