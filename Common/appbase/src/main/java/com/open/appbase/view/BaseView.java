package com.open.appbase.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by vivian on 2017/6/20.
 */

public abstract class BaseView extends View {
    protected Reference<Context> mContext;

    // half size of view, may used for canvas.translate(halfWidth, halfHeight);
    protected int halfWidth;
    protected int halfHeight;

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseView(Context context) {
        this(context, null);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = new WeakReference<>(context);
        setAttrs(attrs);
        initData();
    }

    /**
     * init attrs
     * use it like
     * try {
     * TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CircleVolume);
     * unitCount = typedArray.getInteger(R.styleable.CircleVolume_unit_count, 10);
     * unitSpace = typedArray.getInteger(R.styleable.CircleVolume_unit_space, 5);
     * normalColor = typedArray.getColor(R.styleable.CircleVolume_normal_color, Color.BLACK);
     * focusColor = typedArray.getColor(R.styleable.CircleVolume_focus_color, Color.GREEN);
     * typedArray.recycle();
     * } catch (Exception ex) {
     * Log.w(TAG, "setAttrs ex", ex);
     * }
     *
     * @param attrs view attrs
     */
    protected abstract void setAttrs(AttributeSet attrs);

    /**
     * Init paints, etc.
     * use it like
     * protected void initView() {
             arcPaint = new Paint();
             arcPaint.setAntiAlias(true); // 消除锯齿
             arcPaint.setStrokeWidth(strokeWidth);
             arcPaint.setStrokeCap(Paint.Cap.ROUND); // 设置圆头
             arcPaint.setAntiAlias(true); // 消除锯齿
             arcPaint.setStyle(Paint.Style.STROKE); // 设置空心
         }
     */
    protected abstract void initData();

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        halfWidth = getWidth() / 2;
        halfHeight = getHeight() / 2;

        initSize();
    }

    /**
     * Init size in onLayout.
     * use it like
     *  protected void initSize() {
             radius = Math.min(halfWidth, halfHeight) - strokeWidth;// 获得外圆的半径
             arcRect = new RectF(-radius, -radius, radius, radius);
         }
     */
    protected abstract void initSize();

}
