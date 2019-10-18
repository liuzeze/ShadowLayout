package com.lz.shodowview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;


/**
 * @author : liuze
 * @e-mail : 835052259@qq.com
 * @date : 2019/10/18-9:35
 * @desc : 修改内容
 * @version: 1.0
 */
public class ShadowLayout extends FrameLayout {


    private ShadowHelper mShadowHelper;


    public ShadowLayout(Context context) {
        this(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mShadowHelper = new ShadowHelper(this, attrs);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mShadowHelper.onSizeChanged(w, h);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mShadowHelper.isClipCorner()) {
            canvas.saveLayer(mShadowHelper.getShadowRect(), null, Canvas
                    .ALL_SAVE_FLAG);
            super.dispatchDraw(canvas);
            mShadowHelper.dispatchDraw(canvas);
        } else {
            super.dispatchDraw(canvas);
        }
    }

    public ShadowHelper getShadowHelper() {
        return mShadowHelper;
    }
}
