package com.open.appbase.presenter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * The type Base presenter.
 *
 * @param <V> the type parameter
 */
public abstract class BasePresenter<V> {

    /**
     * The weak reference of V in MVP.
     */
    protected Reference<V> viewWeakRef;

    /**
     * Attach reference.
     *
     * @param ref the ref
     */
    public void attachReference(V ref) {
        viewWeakRef = new WeakReference<>(ref);
    }

    /**
     * Detach reference.
     */
    public void detachReference() {
        if (viewWeakRef != null) {
            viewWeakRef.clear();
            viewWeakRef = null;
        }
    }

    /**
     * Gets outer reference.
     *
     * @return the outer reference
     */
    protected V getOuterReference() {
        return viewWeakRef.get();
    }

    /**
     * Is out reference active boolean.
     *
     * @return the boolean
     */
    public boolean isOutReferenceActive() {
        return viewWeakRef != null && viewWeakRef.get() != null;
    }
}
