package com.learn.blockmonitor.share.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created on 2018/2/28.
 * lazy fragment: only load data if user visible fragment and data not load complete
 * you must setDataLoadCompleted(true), when your data load complete, otherwise it will load every time
 */
public abstract class BaseMVPLazyFragment<V, T extends BasePresenter<V>> extends BaseLazyFragment {
    protected T mPresenter; // presenter

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create presenter
        mPresenter = createPresenter();
        mPresenter.attachReference((V) this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachReference();
    }

    protected abstract T createPresenter();

}