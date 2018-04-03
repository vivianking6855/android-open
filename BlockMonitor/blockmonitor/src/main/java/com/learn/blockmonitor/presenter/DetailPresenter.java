package com.learn.blockmonitor.presenter;

import android.content.Intent;

import com.learn.blockmonitor.share.base.BasePresenter;
import com.learn.blockmonitor.share.util.Const;
import com.learn.blockmonitor.view.ILogDisplayer;

public class DetailPresenter extends BasePresenter<ILogDisplayer>{

    public void getDetailLog(Intent intent){
        showLogFromIntent(intent);
    }

    private void showLogFromIntent(Intent intent) {
        String content = "";
        if (intent != null) {
            content = intent.getStringExtra(Const.KEY_DETAIL_LOG);
        }
        mOuterWeakRef.get().onDisplay(content);
    }
}
