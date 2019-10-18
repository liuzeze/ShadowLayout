package com.lz.shodowview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;


/**
 * @author : liuze
 * @e-mail : 835052259@qq.com
 * @date : 2019/10/18-9:35
 * @desc : 修改内容
 * @version: 1.0
 */
public class ShadowTextView extends AppCompatTextView {


    private ShadowHelper mShadowHelper;

    public ShadowTextView(Context context) {
        this(context, null);
    }

    public ShadowTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ShadowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mShadowHelper = new ShadowHelper(this, attrs);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mShadowHelper.setShadowBackground(w, h);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(mShadowHelper.getClipPath());
        super.onDraw(canvas);
        canvas.restore();
    }


}
