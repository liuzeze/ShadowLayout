package com.lz.shodowview;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author : liuze
 * @e-mail : 835052259@qq.com
 * @date : 2019/10/18-9:35
 * @desc : 修改内容
 * @version: 1.0
 */
public class ShadowHelper {

    private final View mView;
    private int mBackGroundColor;
    private int mShadowColor;
    private int mStrokeColor;
    private int mSelectStrokeColor;

    private int mSelectBackGroundColor;
    private int mSelectShadowColor;
    private float mShadowBlur;
    private float mCornerRadius;
    private float mOffsetX;
    private float mOffsetY;

    private boolean mLeftShow;
    private boolean mRightShow;
    private boolean mTopShow;
    private boolean mBottomShow;

    private Paint mShadowPaint;
    private Paint mPaintStroke;

    //绘制圆角
    private Paint mClipPaint;
    private RectF mShadowRect;
    private float[] mRadii;
    private Path mClipPath = new Path();
    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingRight;
    private int mPaddingBottom;
    private boolean mClipCorner;
    private boolean mSelectEnable;


    public ShadowHelper(View view, AttributeSet attrs) {
        mView = view;
        initAttributes(attrs);

    }


    protected void onSizeChanged(int w, int h) {
        if (w > 0 && h > 0) {
            setShadowBackground(w, h);
        }
    }


    private void initView() {

        //阴影画笔
        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setStyle(Paint.Style.FILL);


        //边线画笔
        mPaintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintStroke.setStyle(Paint.Style.STROKE);
        mPaintStroke.setColor(mStrokeColor);
        mPaintStroke.setStrokeWidth(2);


        //剪裁圆角控件
        mClipPaint = new Paint();
        mClipPaint.setColor(Color.WHITE);
        mClipPaint.setAntiAlias(true);
        // 绘制模式为填充
        mClipPaint.setStyle(Paint.Style.FILL);
        // 混合模式为 DST_IN, 即仅显示当前绘制区域和背景区域交集的部分，并仅显示背景内容。
        mClipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        mRadii = new float[]{mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius};

        mPaddingLeft = mView.getPaddingLeft();
        mPaddingTop = mView.getPaddingTop();
        mPaddingRight = mView.getPaddingRight();
        mPaddingBottom = mView.getPaddingBottom();

        mView.setClickable(true);

        setPading();
    }


    private void initAttributes(AttributeSet attrs) {
        TypedArray attr = mView.getContext().obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        if (attr == null) {
            return;
        }

        try {
            //默认是显示
            mLeftShow = attr.getBoolean(R.styleable.ShadowLayout_sl_leftShow, true);
            mRightShow = attr.getBoolean(R.styleable.ShadowLayout_sl_rightShow, true);
            mBottomShow = attr.getBoolean(R.styleable.ShadowLayout_sl_bottomShow, true);
            mClipCorner = attr.getBoolean(R.styleable.ShadowLayout_sl_clip_cornerr, true);
            mTopShow = attr.getBoolean(R.styleable.ShadowLayout_sl_topShow, true);
            mCornerRadius = attr.getDimension(R.styleable.ShadowLayout_sl_cornerRadius, 0);
            //默认扩散区域宽度
            mShadowBlur = attr.getDimension(R.styleable.ShadowLayout_sl_shadowBlur, 10);

            //x轴偏移量
            mOffsetX = attr.getDimension(R.styleable.ShadowLayout_sl_offsetX, 0);
            //y轴偏移量
            mOffsetY = attr.getDimension(R.styleable.ShadowLayout_sl_offsetY, 8);

            mBackGroundColor = attr.getColor(R.styleable.ShadowLayout_sl_backgroundColor, Color.TRANSPARENT);
            mStrokeColor = attr.getColor(R.styleable.ShadowLayout_sl_strokeColor, Color.TRANSPARENT);
            mShadowColor = attr.getColor(R.styleable.ShadowLayout_sl_shadowColor, Color.TRANSPARENT);
            if (mShadowColor == Color.TRANSPARENT) {
                if (mBackGroundColor != Color.TRANSPARENT) {
                    mShadowColor = mBackGroundColor;
                } else if (mStrokeColor != Color.TRANSPARENT) {
                    mShadowColor = mStrokeColor;
                }
            }

            mSelectBackGroundColor = attr.getColor(R.styleable.ShadowLayout_sl_selectBackgroundColor, Color.TRANSPARENT);
            mSelectShadowColor = attr.getColor(R.styleable.ShadowLayout_sl_selectShadowColor, Color.TRANSPARENT);
            mSelectStrokeColor = attr.getColor(R.styleable.ShadowLayout_sl_selectStrokeColor, Color.TRANSPARENT);

            mSelectEnable = mSelectBackGroundColor != Color.TRANSPARENT
                    || mSelectShadowColor != Color.TRANSPARENT
                    || mSelectStrokeColor != Color.TRANSPARENT;
            if (mSelectEnable) {
                if (mSelectBackGroundColor == Color.TRANSPARENT) {
                    mSelectBackGroundColor = mBackGroundColor;
                }
                if (mSelectShadowColor == Color.TRANSPARENT) {
                    mSelectShadowColor = mShadowColor;
                }
                if (mSelectStrokeColor == Color.TRANSPARENT) {
                    mSelectStrokeColor = mStrokeColor;
                }
            }


        } finally {
            attr.recycle();
        }
        initView();
    }


    public void setPading() {
        int xPadding = (int) (mShadowBlur + Math.abs(mOffsetX));
        int yPadding = (int) (mShadowBlur + Math.abs(mOffsetY));

        int leftPading;
        int topPading;
        int rightPading;
        int bottomPading;

        if (mLeftShow) {
            leftPading = xPadding;
        } else {
            leftPading = 0;
        }

        if (mTopShow) {
            topPading = yPadding;
        } else {
            topPading = 0;
        }


        if (mRightShow) {
            rightPading = xPadding;
        } else {
            rightPading = 0;
        }

        if (mBottomShow) {
            bottomPading = yPadding;
        } else {
            bottomPading = 0;
        }


        mView.setPadding(leftPading + mPaddingLeft, topPading + mPaddingTop, rightPading + mPaddingRight, bottomPading + mPaddingBottom);
    }


    public void setShadowBackground(int w, int h) {

        Bitmap unBitmap = createShadowBitmap(w, h, mCornerRadius, mShadowBlur, mOffsetX, mOffsetY, mShadowColor, mBackGroundColor, mStrokeColor);
        BitmapDrawable unDrawable = new BitmapDrawable(unBitmap);


        if (mSelectEnable) {
            Bitmap bitmap = createShadowBitmap(w, h, mCornerRadius, mShadowBlur, mOffsetX, mOffsetY, mSelectShadowColor, mSelectBackGroundColor, mSelectStrokeColor);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);


            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, drawable);
            stateListDrawable.addState(new int[]{android.R.attr.state_checked}, drawable);
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, drawable);
            stateListDrawable.addState(new int[]{}, unDrawable);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                mView.setBackgroundDrawable(stateListDrawable);
            } else {
                mView.setBackground(stateListDrawable);
            }
        } else {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                mView.setBackgroundDrawable(unDrawable);
            } else {
                mView.setBackground(unDrawable);
            }
        }


    }

    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float cornerRadius, float shadowRadius,
                                      float dx, float dy, int shadowColor, int fillColor, int strokeColor) {

        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        if (mView instanceof ViewGroup) {
            mShadowRect = new RectF(
                    shadowRadius + mPaddingLeft,
                    shadowRadius + mPaddingTop,
                    shadowWidth - shadowRadius - mPaddingRight,
                    shadowHeight - shadowRadius - mPaddingBottom);
        } else {
            mShadowRect = new RectF(
                    shadowRadius,
                    shadowRadius,
                    shadowWidth - shadowRadius,
                    shadowHeight - shadowRadius);
        }

        if (dx > 0) {
            mShadowRect.left += dx;
            mShadowRect.right -= dx;
        } else if (dx < 0) {

            mShadowRect.left += Math.abs(dx);
            mShadowRect.right -= Math.abs(dx);
        }

        if (dy > 0) {
            mShadowRect.top += dy;
            mShadowRect.bottom -= dy;
        } else if (dy < 0) {
            mShadowRect.top += Math.abs(dy);
            mShadowRect.bottom -= Math.abs(dy);
        }

        mShadowPaint.setColor(fillColor);
        if (!mView.isInEditMode()) {
            mShadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor);
        }

        canvas.drawRoundRect(mShadowRect, cornerRadius, cornerRadius, mShadowPaint);

        if (strokeColor != Color.TRANSPARENT) {
            mPaintStroke.setColor(strokeColor);
            canvas.drawRoundRect(mShadowRect, cornerRadius, cornerRadius, mPaintStroke);
        }

        mClipPath.reset();
        mClipPath.addRoundRect(mShadowRect, mRadii, Path.Direction.CW);

        return output;
    }

    protected void dispatchDraw(Canvas canvas) {
        // 绘制带有圆角的 Path
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            mClipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawPath(mClipPath, mClipPaint);
        } else {
            mClipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

            final Path path = new Path();
            path.addRect(0, 0, (int) mShadowRect.width(), (int) mShadowRect.height(), Path.Direction.CW);
            path.op(mClipPath, Path.Op.DIFFERENCE);
            canvas.drawPath(path, mClipPaint);
        }
        canvas.restore();
    }

    public RectF getShadowRect() {
        return mShadowRect;
    }

    public boolean isClipCorner() {
        return mClipCorner;
    }

    public Path getClipPath() {
        return mClipPath;
    }

    public ShadowHelper setOffsetX(float offsetX) {
        if (Math.abs(offsetX) > mShadowBlur) {
            if (offsetX > 0) {
                this.mOffsetX = mShadowBlur;
            } else {
                this.mOffsetX = -mShadowBlur;
            }
        } else {
            this.mOffsetX = offsetX;
        }

        return this;
    }

    public ShadowHelper setOffsetY(float offsetY) {
        if (Math.abs(offsetY) > mShadowBlur) {
            if (offsetY > 0) {
                this.mOffsetY = mShadowBlur;
            } else {
                this.mOffsetY = -mShadowBlur;
            }
        } else {
            this.mOffsetY = offsetY;
        }
        return this;
    }


    public float getmCornerRadius() {
        return mCornerRadius;
    }


    public void setmCornerRadius(int mCornerRadius) {
        this.mCornerRadius = mCornerRadius;
    }

    public float getShadowBlur() {
        return mShadowBlur;
    }

    public ShadowHelper setShadowBlur(float shadowBlur) {
        mShadowBlur = shadowBlur;

        return this;
    }

    public ShadowHelper setBackGroundColor(int backGroundColor) {
        mBackGroundColor = backGroundColor;
        return this;
    }

    public ShadowHelper setShadowColor(int shadowColor) {
        mShadowColor = shadowColor;
        return this;
    }

    public ShadowHelper setStrokeColor(int strokeColor) {
        mStrokeColor = strokeColor;
        return this;
    }

    public ShadowHelper setSelectStrokeColor(int selectStrokeColor) {
        mSelectStrokeColor = selectStrokeColor;
        return this;
    }

    public ShadowHelper setSelectBackGroundColor(int selectBackGroundColor) {
        mSelectBackGroundColor = selectBackGroundColor;
        return this;
    }

    public ShadowHelper setSelectShadowColor(int selectShadowColor) {
        mSelectShadowColor = selectShadowColor;
        return this;
    }

    public void setLeftShow(boolean leftShow) {
        this.mLeftShow = leftShow;
    }

    public void setRightShow(boolean rightShow) {
        this.mRightShow = rightShow;
    }

    public void setTopShow(boolean topShow) {
        this.mTopShow = topShow;
    }

    public void setBottomShow(boolean bottomShow) {
        this.mBottomShow = bottomShow;
    }

    public void apply() {
        mView.post(new Runnable() {
            @Override
            public void run() {
                setPading();
                setShadowBackground(mView.getWidth(), mView.getHeight());
            }
        });
    }
}
