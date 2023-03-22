package nemosofts.driving.exam.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Checkable;
import nemosofts.driving.exam.BuildConfig;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.utils.NemosoftsUtil;

/* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/view/SmoothCheckBox.class */
public class SmoothCheckBox extends View implements Checkable {
    private static final String KEY_INSTANCE_STATE = "InstanceState";
    private static final int COLOR_TICK = -1;
    private static final int COLOR_UNCHECKED = -1;
    private static final int COLOR_CHECKED = Color.parseColor("#FB4846");
    private static final int COLOR_FLOOR_UNCHECKED = Color.parseColor("#DFDFDF");
    private static final int DEF_DRAW_SIZE = 25;
    private static final int DEF_ANIM_DURATION = 300;
    private Paint mPaint;
    private Paint mTickPaint;
    private Paint mFloorPaint;
    private Point[] mTickPoints;
    private Point mCenterPoint;
    private Path mTickPath;
    private float mLeftLineDistance;
    private float mRightLineDistance;
    private float mDrewDistance;
    private float mScaleVal;
    private float mFloorScale;
    private int mWidth;
    private int mAnimDuration;
    private int mStrokeWidth;
    private int mCheckedColor;
    private int mUnCheckedColor;
    private int mFloorColor;
    private int mFloorUnCheckedColor;
    private boolean mChecked;
    private boolean mTickDrawing;
    private OnCheckedChangeListener mListener;
    private boolean misRect;

    /* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/view/SmoothCheckBox$OnCheckedChangeListener.class */
    public interface OnCheckedChangeListener {
        void onCheckedChanged(SmoothCheckBox smoothCheckBox, boolean z);
    }

    public SmoothCheckBox(Context context) {
        this(context, null);
    }

    public SmoothCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmoothCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mScaleVal = 1.0f;
        this.mFloorScale = 1.0f;
        this.misRect = false;
        init(attrs);
    }

    public SmoothCheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mScaleVal = 1.0f;
        this.mFloorScale = 1.0f;
        this.misRect = false;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SmoothCheckBox);
        int tickColor = ta.getColor(R.styleable.SmoothCheckBox_smoothCheckBox_color_tick, -1);
        this.mAnimDuration = ta.getInt(R.styleable.SmoothCheckBox_smoothCheckBox_duration, DEF_ANIM_DURATION);
        this.mFloorColor = ta.getColor(R.styleable.SmoothCheckBox_smoothCheckBox_color_unchecked_stroke, COLOR_FLOOR_UNCHECKED);
        this.mCheckedColor = ta.getColor(R.styleable.SmoothCheckBox_smoothCheckBox_color_checked, COLOR_CHECKED);
        this.mUnCheckedColor = ta.getColor(R.styleable.SmoothCheckBox_smoothCheckBox_color_unchecked, -1);
        this.mStrokeWidth = ta.getDimensionPixelSize(R.styleable.SmoothCheckBox_smoothCheckBox_stroke_width, NemosoftsUtil.dp2px(getContext(), 0.0f));
        this.misRect = ta.getBoolean(R.styleable.SmoothCheckBox_smoothCheckBox_is_rect, this.misRect);
        ta.recycle();
        this.mFloorUnCheckedColor = this.mFloorColor;
        this.mTickPaint = new Paint(1);
        this.mTickPaint.setStyle(Paint.Style.STROKE);
        this.mTickPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mTickPaint.setColor(tickColor);
        this.mFloorPaint = new Paint(1);
        this.mFloorPaint.setStyle(Paint.Style.FILL);
        this.mFloorPaint.setColor(this.mFloorColor);
        this.mPaint = new Paint(1);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(this.mCheckedColor);
        this.mTickPath = new Path();
        this.mCenterPoint = new Point();
        this.mTickPoints = new Point[3];
        this.mTickPoints[0] = new Point();
        this.mTickPoints[1] = new Point();
        this.mTickPoints[2] = new Point();
        setOnClickListener(v -> {
            toggle();
            this.mTickDrawing = false;
            this.mDrewDistance = 0.0f;
            if (isChecked()) {
                startCheckedAnimation();
            } else {
                startUnCheckedAnimation();
            }
        });
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putBoolean(KEY_INSTANCE_STATE, isChecked());
        return bundle;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            boolean isChecked = bundle.getBoolean(KEY_INSTANCE_STATE);
            setChecked(isChecked);
            super.onRestoreInstanceState(bundle.getParcelable(KEY_INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return this.mChecked;
    }

    @Override // android.widget.Checkable
    public void toggle() {
        setChecked(!isChecked());
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean checked) {
        this.mChecked = checked;
        reset();
        invalidate();
        if (this.mListener != null) {
            this.mListener.onCheckedChanged(this, this.mChecked);
        }
    }

    public void setChecked(boolean checked, boolean animate) {
        if (animate) {
            this.mTickDrawing = false;
            this.mChecked = checked;
            this.mDrewDistance = 0.0f;
            if (checked) {
                startCheckedAnimation();
            } else {
                startUnCheckedAnimation();
            }
            if (this.mListener != null) {
                this.mListener.onCheckedChanged(this, this.mChecked);
                return;
            }
            return;
        }
        setChecked(checked);
    }

    private void reset() {
        this.mTickDrawing = true;
        this.mFloorScale = 1.0f;
        this.mScaleVal = isChecked() ? 0.0f : 1.0f;
        this.mFloorColor = isChecked() ? this.mCheckedColor : this.mFloorUnCheckedColor;
        this.mDrewDistance = isChecked() ? this.mLeftLineDistance + this.mRightLineDistance : 0.0f;
    }

    private int measureSize(int measureSpec) {
        int defSize = NemosoftsUtil.dp2px(getContext(), 25.0f);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int result = 0;
//        switch (specMode) {
//            case Integer.MIN_VALUE:
//            case BuildConfig.DEBUG /* 0 */:
//                result = Math.min(defSize, specSize);
//                break;
//            case 1073741824:
//                result = specSize;
//                break;
//        }
        return result;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
    }

    @Override // android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.mWidth = getMeasuredWidth();
        this.mStrokeWidth = this.mStrokeWidth == 0 ? getMeasuredWidth() / 10 : this.mStrokeWidth;
        this.mStrokeWidth = this.mStrokeWidth > getMeasuredWidth() / 5 ? getMeasuredWidth() / 5 : this.mStrokeWidth;
        this.mStrokeWidth = this.mStrokeWidth < 3 ? 3 : this.mStrokeWidth;
        this.mCenterPoint.x = this.mWidth / 2;
        this.mCenterPoint.y = getMeasuredHeight() / 2;
        this.mTickPoints[0].x = Math.round((getMeasuredWidth() / 30.0f) * 7.0f);
        this.mTickPoints[0].y = Math.round((getMeasuredHeight() / 30.0f) * 14.0f);
        this.mTickPoints[1].x = Math.round((getMeasuredWidth() / 30.0f) * 13.0f);
        this.mTickPoints[1].y = Math.round((getMeasuredHeight() / 30.0f) * 20.0f);
        this.mTickPoints[2].x = Math.round((getMeasuredWidth() / 30.0f) * 22.0f);
        this.mTickPoints[2].y = Math.round((getMeasuredHeight() / 30.0f) * 10.0f);
        this.mLeftLineDistance = (float) Math.sqrt(Math.pow(this.mTickPoints[1].x - this.mTickPoints[0].x, 2.0d) + Math.pow(this.mTickPoints[1].y - this.mTickPoints[0].y, 2.0d));
        this.mRightLineDistance = (float) Math.sqrt(Math.pow(this.mTickPoints[2].x - this.mTickPoints[1].x, 2.0d) + Math.pow(this.mTickPoints[2].y - this.mTickPoints[1].y, 2.0d));
        this.mTickPaint.setStrokeWidth(this.mStrokeWidth);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        drawBorder(canvas);
        drawCenter(canvas);
        drawTick(canvas);
    }

    private void drawCenter(Canvas canvas) {
        this.mPaint.setColor(this.mUnCheckedColor);
        if (this.misRect) {
            float width = Math.min(getWidth(), getHeight());
            float twmp_width = (width * this.mScaleVal) - this.mStrokeWidth;
            canvas.drawRect(width - twmp_width, width - twmp_width, twmp_width, twmp_width, this.mPaint);
            return;
        }
        float radius = (this.mCenterPoint.x - this.mStrokeWidth) * this.mScaleVal;
        canvas.drawCircle(this.mCenterPoint.x, this.mCenterPoint.y, radius, this.mPaint);
    }

    private void drawBorder(Canvas canvas) {
        this.mFloorPaint.setColor(this.mFloorColor);
        if (this.misRect) {
            float width = Math.min(getWidth(), getHeight());
            float twmp_width = width * this.mFloorScale;
            canvas.drawRect(width - twmp_width, width - twmp_width, twmp_width, twmp_width, this.mFloorPaint);
            return;
        }
        int radius = this.mCenterPoint.x;
        canvas.drawCircle(this.mCenterPoint.x, this.mCenterPoint.y, radius * this.mFloorScale, this.mFloorPaint);
    }

    private void drawTick(Canvas canvas) {
        if (this.mTickDrawing && isChecked()) {
            drawTickPath(canvas);
        }
    }

    private void drawTickPath(Canvas canvas) {
        this.mTickPath.reset();
        if (this.mDrewDistance < this.mLeftLineDistance) {
            float step = ((float) this.mWidth) / 20.0f < 3.0f ? 3.0f : this.mWidth / 20.0f;
            this.mDrewDistance += step;
            float stopX = this.mTickPoints[0].x + (((this.mTickPoints[1].x - this.mTickPoints[0].x) * this.mDrewDistance) / this.mLeftLineDistance);
            float stopY = this.mTickPoints[0].y + (((this.mTickPoints[1].y - this.mTickPoints[0].y) * this.mDrewDistance) / this.mLeftLineDistance);
            this.mTickPath.moveTo(this.mTickPoints[0].x, this.mTickPoints[0].y);
            this.mTickPath.lineTo(stopX, stopY);
            canvas.drawPath(this.mTickPath, this.mTickPaint);
            if (this.mDrewDistance > this.mLeftLineDistance) {
                this.mDrewDistance = this.mLeftLineDistance;
            }
        } else {
            this.mTickPath.moveTo(this.mTickPoints[0].x, this.mTickPoints[0].y);
            this.mTickPath.lineTo(this.mTickPoints[1].x, this.mTickPoints[1].y);
            canvas.drawPath(this.mTickPath, this.mTickPaint);
            if (this.mDrewDistance < this.mLeftLineDistance + this.mRightLineDistance) {
                float stopX2 = this.mTickPoints[1].x + (((this.mTickPoints[2].x - this.mTickPoints[1].x) * (this.mDrewDistance - this.mLeftLineDistance)) / this.mRightLineDistance);
                float stopY2 = this.mTickPoints[1].y - (((this.mTickPoints[1].y - this.mTickPoints[2].y) * (this.mDrewDistance - this.mLeftLineDistance)) / this.mRightLineDistance);
                this.mTickPath.reset();
                this.mTickPath.moveTo(this.mTickPoints[1].x, this.mTickPoints[1].y);
                this.mTickPath.lineTo(stopX2, stopY2);
                canvas.drawPath(this.mTickPath, this.mTickPaint);
                float step2 = this.mWidth / 20 < 3 ? 3.0f : this.mWidth / 20;
                this.mDrewDistance += step2;
            } else {
                this.mTickPath.reset();
                this.mTickPath.moveTo(this.mTickPoints[1].x, this.mTickPoints[1].y);
                this.mTickPath.lineTo(this.mTickPoints[2].x, this.mTickPoints[2].y);
                canvas.drawPath(this.mTickPath, this.mTickPaint);
            }
        }
        if (this.mDrewDistance < this.mLeftLineDistance + this.mRightLineDistance) {
            postDelayed(() -> {
                postInvalidate();
            }, 10L);
        }
    }

    private void startCheckedAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.0f);
        animator.setDuration((this.mAnimDuration / 3) * 2);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            this.mScaleVal = ((Float) animation.getAnimatedValue()).floatValue();
            this.mFloorColor = getGradientColor(this.mUnCheckedColor, this.mCheckedColor, 1.0f - this.mScaleVal);
            postInvalidate();
        });
        animator.start();
        ValueAnimator floorAnimator = ValueAnimator.ofFloat(1.0f, 0.8f, 1.0f);
        floorAnimator.setDuration(this.mAnimDuration);
        floorAnimator.setInterpolator(new LinearInterpolator());
        floorAnimator.addUpdateListener(animation2 -> {
            this.mFloorScale = ((Float) animation2.getAnimatedValue()).floatValue();
            postInvalidate();
        });
        floorAnimator.start();
        drawTickDelayed();
    }

    private void startUnCheckedAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setDuration(this.mAnimDuration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            this.mScaleVal = ((Float) animation.getAnimatedValue()).floatValue();
            this.mFloorColor = getGradientColor(this.mCheckedColor, this.mFloorUnCheckedColor, this.mScaleVal);
            postInvalidate();
        });
        animator.start();
        ValueAnimator floorAnimator = ValueAnimator.ofFloat(1.0f, 0.8f, 1.0f);
        floorAnimator.setDuration(this.mAnimDuration);
        floorAnimator.setInterpolator(new LinearInterpolator());
        floorAnimator.addUpdateListener(animation2 -> {
            this.mFloorScale = ((Float) animation2.getAnimatedValue()).floatValue();
            postInvalidate();
        });
        floorAnimator.start();
    }

    private void drawTickDelayed() {
        postDelayed(() -> {
            this.mTickDrawing = true;
            postInvalidate();
        }, this.mAnimDuration);
    }

    private static int getGradientColor(int startColor, int endColor, float percent) {
        int startA = Color.alpha(startColor);
        int startR = Color.red(startColor);
        int startG = Color.green(startColor);
        int startB = Color.blue(startColor);
        int endA = Color.alpha(endColor);
        int endR = Color.red(endColor);
        int endG = Color.green(endColor);
        int endB = Color.blue(endColor);
        int currentA = (int) ((startA * (1.0f - percent)) + (endA * percent));
        int currentR = (int) ((startR * (1.0f - percent)) + (endR * percent));
        int currentG = (int) ((startG * (1.0f - percent)) + (endG * percent));
        int currentB = (int) ((startB * (1.0f - percent)) + (endB * percent));
        return Color.argb(currentA, currentR, currentG, currentB);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener l) {
        this.mListener = l;
    }
}
