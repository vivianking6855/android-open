package com.learn.blockmonitor.presenter;

import android.util.Log;

import com.learn.blockmonitor.model.BlockStackModel;
import com.learn.blockmonitor.model.FileLogProvider;
import com.learn.blockmonitor.model.ILogProvider;
import com.learn.blockmonitor.share.base.BasePresenter;
import com.learn.blockmonitor.share.util.Const;
import com.learn.blockmonitor.view.ILogDisplayer;

import java.util.List;

public class LogListPresenter extends BasePresenter<ILogDisplayer>{

    private ILogProvider<List<BlockStackModel>,String> logProvider = new FileLogProvider();

    public void fetchData(){
        logProvider.fetchLogAsync(new ILogProvider.IFetchLogListener<List<BlockStackModel>,String>() {
            @Override
            public void onSuccess(List<BlockStackModel> data) {
                mOuterWeakRef.get().onDisplay(data);
            }

            @Override
            public void onError(String error) {
                Log.e(Const.BLOCK_TAG,"fetch log error:"+error);
            }
        });
    }
}
