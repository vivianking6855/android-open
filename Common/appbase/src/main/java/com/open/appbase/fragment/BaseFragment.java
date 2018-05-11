package com.open.appbase.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created on 2018/2/28.
 * lazy fragment: only load data if user visible fragment and data not load complete
 * you must setDataLoadCompleted(true), when your data load complete, otherwise it will load every time
 */
public abstract class BaseFragment extends Fragment {

    protected Reference<FragmentActivity> mActivityRef;

    /**
     * fragment layout.
     *
     * @return the layout
     */
    protected abstract int getLayout();

    /**
     * Init data.
     */
    protected abstract void initData();

    /**
     * Init views.
     *
     * @param view the view
     */
    protected abstract void initViews(View view);

    /**
     * Load data.
     */
    protected abstract void loadData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflater view
        View view = inflater.inflate(getLayout(), container, false);

        initData();
        initViews(view);
        loadData();

        mActivityRef = new WeakReference<>(getActivity());

        return view;
    }
}