package com.wenxi.learn.demo.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wenxi.learn.demo.R;
import com.wenxi.learn.demo.adapter.HomeRecyclerAdapter;
import com.wenxi.learn.demo.base.BaseActivity;
import com.wenxi.learn.demo.model.HomeModel;

import java.util.Arrays;


public class MainActivity extends BaseActivity {
    private HomeRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.algorithm_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HomeRecyclerAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void loadData() {
        HomeModel model = new HomeModel();
        model.titleArray = Arrays.asList(getResources().getStringArray(R.array.algorithm_list));
        mAdapter.setData(model);
    }
}
