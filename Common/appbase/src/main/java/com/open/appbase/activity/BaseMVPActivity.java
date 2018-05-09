package com.open.appbase.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.open.appbase.presenter.BasePresenter;

/**
 * The type Base mvp activity.
 * you need implement createPresenter to create Presenter
 * which must extends {@link BasePresenter}
 * or you can Override onCreate to customized your attachReference
 *
 * @param <V> the type parameter
 * @param <P> the type parameter
 */
public abstract class BaseMVPActivity<V, P extends BasePresenter<V>> extends BaseActivity {
    /**
     * The presenter P in MPV
     */
    protected P mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // create presenter
        mPresenter = createPresenter();
        mPresenter.attachReference((V) this);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachReference();
    }

    /**
     * Create presenter t that extends {@link BasePresenter}
     *
     * @return the t
     */
    protected abstract P createPresenter();
}
