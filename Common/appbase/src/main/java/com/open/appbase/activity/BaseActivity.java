package com.open.appbase.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author vivian
 *         BaseActivity used for project refactoring
 *         initData
 *         initView
 *         loadData
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        initData();
        initView(savedInstanceState);
        loadData();
    }

    /**
     * set layout data.
     */
    protected abstract int getLayout();

    /**
     * Init data.
     */
    protected abstract void initData();

    /**
     * Init view.
     * please setContentView here
     *
     * @param savedInstanceState the saved instance state
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * Load data.
     */
    protected abstract void loadData();

    /**
     * Add fragment with fragmentTransaction.commitAllowingStateLoss
     *
     * @param containerViewId the container view id
     * @param fragment        the fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        final FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

}

