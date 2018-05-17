package com.open.appbase.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.open.appbase.presenter.BasePresenter;

/**
 * Created on 2018/5/15.
 * lazy fragment: only load data if user visible fragment and data not load complete
 * you must setDataLoadCompleted(true), when your data load complete, otherwise it will load every time
 *
 * @param <V> the type parameter
 * @param <T> the type parameter
 */
public abstract class BaseMVPFragment<V, T extends BasePresenter<V>> extends BaseFragment {
    /**
     * The presenter P in MVP
     */
    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // create presenter
        mPresenter = createPresenter();
        mPresenter.attachReference((V) this);

        super.onCreate(savedInstanceState);
    }

    /**
     * Create presenter t that extends {@link BasePresenter}
     *
     * @return the t
     */
    protected abstract T createPresenter();

}