package com.jiagu.mobile.tourguide.widget.progressbar;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class CircularProgressDrawable extends Drawable implements Animatable {
    
    /**
     * 绘制圆弧起始位置角度的动画，这样该圆弧是打圈转的动画
     */
    private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
    /**
     * 绘制圆弧臂长的动画，该动画受 mModeAppearing 控制�?
     * �? mModeAppearing �? false 的时候，圆弧的起始点在增加，圆弧的终止点不变，弧长在逐渐减少�?
     * �? mModeAppearing �? true 的时候， 圆弧的起始点不变，圆弧的终止点变大，弧长在�?�渐增加
     */
    private static final Interpolator SWEEP_INTERPOLATOR = new DecelerateInterpolator();
    /**
     * 圆弧起始位置动画的间隔，也就是多少毫秒圆弧转�?圈，可以把该值扩�?10倍来查看动画的慢动作
     */
    private static final int ANGLE_ANIMATOR_DURATION = 2000;
    /**
     * 圆弧臂长的动画间隔，也就是臂长从�?小到�?大�?�的变化时间，也可以把该值扩�?10倍来查看动画的慢动作
     */
    private static final int SWEEP_ANIMATOR_DURATION = 600;
    /**
     * 圆弧的最下臂长是多少�?
     */
    private static final int MIN_SWEEP_ANGLE = 30;
    private final RectF fBounds = new RectF();

    /**
     * 起始位置的动画对�?
     */
    private ObjectAnimator mObjectAnimatorSweep;
    /**
     * 臂长的动画对�?
     */
    private ObjectAnimator mObjectAnimatorAngle;
    /**
     * 控制臂长是�?�渐增加还是逐渐减少
     */
    private boolean mModeAppearing;
    private Paint mPaint;
    /**
     * 每次臂长增加 、减�? 转换的时候， 圆弧起始位置的偏移量会增�? 2 倍的�?小臂�?
     */
    private float mCurrentGlobalAngleOffset;
    private float mCurrentGlobalAngle;
    private float mCurrentSweepAngle;
    private float mBorderWidth;
    private boolean mRunning;

    public CircularProgressDrawable(int color, float borderWidth) {
        mBorderWidth = borderWidth;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setColor(color);

        setupAnimations();
    }

    @Override
    public void draw(Canvas canvas) {
        float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset;
        float sweepAngle = mCurrentSweepAngle;
        if (mModeAppearing) {
            sweepAngle += MIN_SWEEP_ANGLE;
        } else {
            startAngle = startAngle + sweepAngle;
            sweepAngle = 360 - sweepAngle - MIN_SWEEP_ANGLE;
        }
        canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    private void toggleAppearingMode() {
        mModeAppearing = !mModeAppearing;
        if (mModeAppearing) {
            mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360;
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        fBounds.left = bounds.left + mBorderWidth / 2f + .5f;
        fBounds.right = bounds.right - mBorderWidth / 2f - .5f;
        fBounds.top = bounds.top + mBorderWidth / 2f + .5f;
        fBounds.bottom = bounds.bottom - mBorderWidth / 2f - .5f;
    }

    // ////////////////////////////////////////////////////////////////////////////
    // ////////////// Animation

    private Property<CircularProgressDrawable, Float> mAngleProperty = new Property<CircularProgressDrawable, Float>(Float.class, "angle") {
        @Override
        public Float get(CircularProgressDrawable object) {
            return object.getCurrentGlobalAngle();
        }

        @Override
        public void set(CircularProgressDrawable object, Float value) {
            object.setCurrentGlobalAngle(value);
        }
    };

    private Property<CircularProgressDrawable, Float> mSweepProperty = new Property<CircularProgressDrawable, Float>(Float.class, "arc") {
        @Override
        public Float get(CircularProgressDrawable object) {
            return object.getCurrentSweepAngle();
        }

        @Override
        public void set(CircularProgressDrawable object, Float value) {
            object.setCurrentSweepAngle(value);
        }
    };

    private void setupAnimations() {
        mObjectAnimatorAngle = ObjectAnimator.ofFloat(this, mAngleProperty, 360f);
        mObjectAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
        mObjectAnimatorAngle.setDuration(ANGLE_ANIMATOR_DURATION);
        mObjectAnimatorAngle.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimatorAngle.setRepeatCount(ValueAnimator.INFINITE);

        mObjectAnimatorSweep = ObjectAnimator.ofFloat(this, mSweepProperty, 360f - MIN_SWEEP_ANGLE * 2);
        mObjectAnimatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
        mObjectAnimatorSweep.setDuration(SWEEP_ANIMATOR_DURATION);
        mObjectAnimatorSweep.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimatorSweep.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimatorSweep.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                toggleAppearingMode();
            }
        });
    }

    @Override
    public void start() {
        if (isRunning()) {
            return;
        }
        mRunning = true;
        // 为了方便测试，可以注释掉下面两个动画中的�?个，�?
        //分别查看每个独立的动画是如何运动�?
        mObjectAnimatorAngle.start();
        mObjectAnimatorSweep.start();
        invalidateSelf();
    }

    @Override
    public void stop() {
        if (!isRunning()) {
            return;
        }
        mRunning = false;
        mObjectAnimatorAngle.cancel();
        mObjectAnimatorSweep.cancel();
        invalidateSelf();
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    public void setCurrentGlobalAngle(float currentGlobalAngle) {
        mCurrentGlobalAngle = currentGlobalAngle;
        invalidateSelf();
    }

    public float getCurrentGlobalAngle() {
        return mCurrentGlobalAngle;
    }

    public void setCurrentSweepAngle(float currentSweepAngle) {
        mCurrentSweepAngle = currentSweepAngle;
        invalidateSelf();
    }

    public float getCurrentSweepAngle() {
        return mCurrentSweepAngle;
    }

}
