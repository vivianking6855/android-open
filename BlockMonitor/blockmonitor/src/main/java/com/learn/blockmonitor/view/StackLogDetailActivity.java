package com.learn.blockmonitor.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.widget.TextView;

import com.learn.blockmonitor.R;
import com.learn.blockmonitor.presenter.DetailPresenter;
import com.learn.blockmonitor.share.base.BaseMVPActivity;

/**
 * StackLogDetail Activity
 */

public class StackLogDetailActivity extends BaseMVPActivity<ILogDisplayer,DetailPresenter> implements ILogDisplayer<String>{

    private TextView content;

    @Override
    protected DetailPresenter createPresenter() {
        return new DetailPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.stacklog_detail_page;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        content = findViewById(R.id.detail_content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            content.setBreakStrategy(Layout.BREAK_STRATEGY_SIMPLE);
        }
    }

    @Override
    protected void loadData() {
        mPresenter.getDetailLog(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPresenter.getDetailLog(getIntent());
    }

    @Override
    public void onDisplay(String data) {
        if(data!= null){
            content.setText(data);
        }
    }
}
