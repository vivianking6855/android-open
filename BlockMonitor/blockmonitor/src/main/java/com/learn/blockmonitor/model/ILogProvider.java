package com.learn.blockmonitor.model;

/**
 * Created by Venjee_Shen on 2018/4/3.
 *
 */

public interface ILogProvider<T,M> {

    interface IFetchLogListener<T,M>{
        void onSuccess(T data);
        void onError(M error);
    }

    void fetchLogAsync(IFetchLogListener<T,M> listener);
}
