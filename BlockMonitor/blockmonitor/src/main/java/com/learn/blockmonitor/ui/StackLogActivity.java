package com.learn.blockmonitor.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.learn.blockmonitor.R;

/**
 * Created by Venjee_Shen on 2018/3/26.
 *
 */

public class StackLogActivity extends AppCompatActivity {

    private static final String TAG_LIST = "list";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_state_pager);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.list_container,new StackLogListFragment(),TAG_LIST);
        ft.commitAllowingStateLoss();
    }
}
