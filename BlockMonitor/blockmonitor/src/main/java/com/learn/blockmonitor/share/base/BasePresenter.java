package com.learn.blockmonitor.share.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public abstract class BasePresenter<V> {
    // view week reference
    protected Reference<V> mOuterWeakRef;

    public void attachReference(V ref) {
        mOuterWeakRef = new WeakReference<>(ref);
    }

    public void detachReference() {
        if (mOuterWeakRef != null) {
            mOuterWeakRef.clear();
            mOuterWeakRef = null;
        }
    }

    protected V getOuterReference() {
        return mOuterWeakRef.get();
    }

    public boolean isOutReferenceActive() {
        return mOuterWeakRef != null && mOuterWeakRef.get() != null;
    }
}
