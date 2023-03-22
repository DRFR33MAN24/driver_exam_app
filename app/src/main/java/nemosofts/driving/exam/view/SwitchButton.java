package nemosofts.driving.exam.view;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;
import androidx.annotation.NonNull;
import nemosofts.driving.exam.BuildConfig;
import nemosofts.driving.exam.R;

/* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/view/SwitchButton.class */
public class SwitchButton extends View implements Checkable {
    private static final int DEFAULT_WIDTH = dp2pxInt(58.0f);
    private static final int DEFAULT_HEIGHT = dp2pxInt(36.0f);
    private  int ANIMATE_STATE_NONE = 0;
    private  int ANIMATE_STATE_PENDING_DRAG = 1;
    private  int ANIMATE_STATE_DRAGING = 2;
    private  int ANIMATE_STATE_PENDING_RESET = 3;
    private  int ANIMATE_STATE_PENDING_SETTLE = 4;
    private  int ANIMATE_STATE_SWITCH = 5;
    private int shadowRadius;
    private int shadowOffset;
    private int shadowColor;
    private float viewRadius;
    private float buttonRadius;
    private float height;
    private float width;
    private float left;
    private float top;
    private float right;
    private float bottom;
    private float centerX;
    private float centerY;
    private int background;
    private int uncheckColor;
    private int checkedColor;
    private int borderWidth;
    private int checkLineColor;
    private int checkLineWidth;
    private float checkLineLength;
    private int uncheckCircleColor;
    private int uncheckCircleWidth;
    private float uncheckCircleOffsetX;
    private float uncheckCircleRadius;
    private float checkedLineOffsetX;
    private float checkedLineOffsetY;
    private int uncheckButtonColor;
    private int checkedButtonColor;
    private float buttonMinX;
    private float buttonMaxX;
    private Paint buttonPaint;
    private Paint paint;
    private ViewState viewState;
    private ViewState beforeState;
    private ViewState afterState;
    private RectF rect;
    private int animateState;
    private ValueAnimator valueAnimator;
    private final ArgbEvaluator argbEvaluator;
    private boolean isChecked;
    private boolean enableEffect;
    private boolean shadowEffect;
    private boolean showIndicator;
    private boolean isTouchingDown;
    private boolean isUiInited;
    private boolean isEventBroadcast;
    private OnCheckedChangeListener onCheckedChangeListener;
    private long touchDownTime;
    private Runnable postPendingDrag;
    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener;
    private Animator.AnimatorListener animatorListener;

    /* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/view/SwitchButton$OnCheckedChangeListener.class */
    public interface OnCheckedChangeListener {
        void onCheckedChanged(SwitchButton switchButton, boolean z);
    }

    public SwitchButton(Context context) {
        super(context);
        this.ANIMATE_STATE_NONE = 0;
        this.ANIMATE_STATE_PENDING_DRAG = 1;
        this.ANIMATE_STATE_DRAGING = 2;
        this.ANIMATE_STATE_PENDING_RESET = 3;
        this.ANIMATE_STATE_PENDING_SETTLE = 4;
        this.ANIMATE_STATE_SWITCH = 5;
        this.rect = new RectF();
        this.animateState = 0;
        this.argbEvaluator = new ArgbEvaluator();
        this.isTouchingDown = false;
        this.isUiInited = false;
        this.isEventBroadcast = false;
        this.postPendingDrag = () -> {
            if (!isInAnimating()) {
                pendingDragState();
            }
        };
        this.animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.nemosofts.driving.exam.view.SwitchButton.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
                switch (SwitchButton.this.animateState) {
                    case 1:
                    case 3:
                    case 4:
                        SwitchButton.this.viewState.checkedLineColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(value, Integer.valueOf(SwitchButton.this.beforeState.checkedLineColor), Integer.valueOf(SwitchButton.this.afterState.checkedLineColor))).intValue();
                        SwitchButton.this.viewState.radius = SwitchButton.this.beforeState.radius + ((SwitchButton.this.afterState.radius - SwitchButton.this.beforeState.radius) * value);
                        if (SwitchButton.this.animateState != 1) {
                            SwitchButton.this.viewState.buttonX = SwitchButton.this.beforeState.buttonX + ((SwitchButton.this.afterState.buttonX - SwitchButton.this.beforeState.buttonX) * value);
                        }
                        SwitchButton.this.viewState.checkStateColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(value, Integer.valueOf(SwitchButton.this.beforeState.checkStateColor), Integer.valueOf(SwitchButton.this.afterState.checkStateColor))).intValue();
                        break;
                    case 5:
                        SwitchButton.this.viewState.buttonX = SwitchButton.this.beforeState.buttonX + ((SwitchButton.this.afterState.buttonX - SwitchButton.this.beforeState.buttonX) * value);
                        float fraction = (SwitchButton.this.viewState.buttonX - SwitchButton.this.buttonMinX) / (SwitchButton.this.buttonMaxX - SwitchButton.this.buttonMinX);
                        SwitchButton.this.viewState.checkStateColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(fraction, Integer.valueOf(SwitchButton.this.uncheckColor), Integer.valueOf(SwitchButton.this.checkedColor))).intValue();
                        SwitchButton.this.viewState.radius = fraction * SwitchButton.this.viewRadius;
                        SwitchButton.this.viewState.checkedLineColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(fraction, 0, Integer.valueOf(SwitchButton.this.checkLineColor))).intValue();
                        break;
                }
                SwitchButton.this.postInvalidate();
            }
        };
        this.animatorListener = new Animator.AnimatorListener() { // from class: androidx.nemosofts.driving.exam.view.SwitchButton.2
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                switch (SwitchButton.this.animateState) {
                    case 0 /* 0 */:
                    case 2:
                    default:
                        return;
                    case 1:
                        SwitchButton.this.animateState = 2;
                        SwitchButton.this.viewState.checkedLineColor = 0;
                        SwitchButton.this.viewState.radius = SwitchButton.this.viewRadius;
                        SwitchButton.this.postInvalidate();
                        return;
                    case 3:
                        SwitchButton.this.animateState = 0;
                        SwitchButton.this.postInvalidate();
                        return;
                    case 4:
                        SwitchButton.this.animateState = 0;
                        SwitchButton.this.postInvalidate();
                        SwitchButton.this.broadcastEvent();
                        return;
                    case 5:
                        SwitchButton.this.isChecked = !SwitchButton.this.isChecked;
                        SwitchButton.this.animateState = 0;
                        SwitchButton.this.postInvalidate();
                        SwitchButton.this.broadcastEvent();
                        return;
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animation) {
            }
        };
        init(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ANIMATE_STATE_NONE = 0;
        this.ANIMATE_STATE_PENDING_DRAG = 1;
        this.ANIMATE_STATE_DRAGING = 2;
        this.ANIMATE_STATE_PENDING_RESET = 3;
        this.ANIMATE_STATE_PENDING_SETTLE = 4;
        this.ANIMATE_STATE_SWITCH = 5;
        this.rect = new RectF();
        this.animateState = 0;
        this.argbEvaluator = new ArgbEvaluator();
        this.isTouchingDown = false;
        this.isUiInited = false;
        this.isEventBroadcast = false;
        this.postPendingDrag = () -> {
            if (!isInAnimating()) {
                pendingDragState();
            }
        };
        this.animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.nemosofts.driving.exam.view.SwitchButton.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
                switch (SwitchButton.this.animateState) {
                    case 1:
                    case 3:
                    case 4:
                        SwitchButton.this.viewState.checkedLineColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(value, Integer.valueOf(SwitchButton.this.beforeState.checkedLineColor), Integer.valueOf(SwitchButton.this.afterState.checkedLineColor))).intValue();
                        SwitchButton.this.viewState.radius = SwitchButton.this.beforeState.radius + ((SwitchButton.this.afterState.radius - SwitchButton.this.beforeState.radius) * value);
                        if (SwitchButton.this.animateState != 1) {
                            SwitchButton.this.viewState.buttonX = SwitchButton.this.beforeState.buttonX + ((SwitchButton.this.afterState.buttonX - SwitchButton.this.beforeState.buttonX) * value);
                        }
                        SwitchButton.this.viewState.checkStateColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(value, Integer.valueOf(SwitchButton.this.beforeState.checkStateColor), Integer.valueOf(SwitchButton.this.afterState.checkStateColor))).intValue();
                        break;
                    case 5:
                        SwitchButton.this.viewState.buttonX = SwitchButton.this.beforeState.buttonX + ((SwitchButton.this.afterState.buttonX - SwitchButton.this.beforeState.buttonX) * value);
                        float fraction = (SwitchButton.this.viewState.buttonX - SwitchButton.this.buttonMinX) / (SwitchButton.this.buttonMaxX - SwitchButton.this.buttonMinX);
                        SwitchButton.this.viewState.checkStateColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(fraction, Integer.valueOf(SwitchButton.this.uncheckColor), Integer.valueOf(SwitchButton.this.checkedColor))).intValue();
                        SwitchButton.this.viewState.radius = fraction * SwitchButton.this.viewRadius;
                        SwitchButton.this.viewState.checkedLineColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(fraction, 0, Integer.valueOf(SwitchButton.this.checkLineColor))).intValue();
                        break;
                }
                SwitchButton.this.postInvalidate();
            }
        };
        this.animatorListener = new Animator.AnimatorListener() { // from class: androidx.nemosofts.driving.exam.view.SwitchButton.2
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                switch (SwitchButton.this.animateState) {
                    case 0 /* 0 */:
                    case 2:
                    default:
                        return;
                    case 1:
                        SwitchButton.this.animateState = 2;
                        SwitchButton.this.viewState.checkedLineColor = 0;
                        SwitchButton.this.viewState.radius = SwitchButton.this.viewRadius;
                        SwitchButton.this.postInvalidate();
                        return;
                    case 3:
                        SwitchButton.this.animateState = 0;
                        SwitchButton.this.postInvalidate();
                        return;
                    case 4:
                        SwitchButton.this.animateState = 0;
                        SwitchButton.this.postInvalidate();
                        SwitchButton.this.broadcastEvent();
                        return;
                    case 5:
                        SwitchButton.this.isChecked = !SwitchButton.this.isChecked;
                        SwitchButton.this.animateState = 0;
                        SwitchButton.this.postInvalidate();
                        SwitchButton.this.broadcastEvent();
                        return;
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animation) {
            }
        };
        init(context, attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ANIMATE_STATE_NONE = 0;
        this.ANIMATE_STATE_PENDING_DRAG = 1;
        this.ANIMATE_STATE_DRAGING = 2;
        this.ANIMATE_STATE_PENDING_RESET = 3;
        this.ANIMATE_STATE_PENDING_SETTLE = 4;
        this.ANIMATE_STATE_SWITCH = 5;
        this.rect = new RectF();
        this.animateState = 0;
        this.argbEvaluator = new ArgbEvaluator();
        this.isTouchingDown = false;
        this.isUiInited = false;
        this.isEventBroadcast = false;
        this.postPendingDrag = () -> {
            if (!isInAnimating()) {
                pendingDragState();
            }
        };
        this.animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.nemosofts.driving.exam.view.SwitchButton.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
                switch (SwitchButton.this.animateState) {
                    case 1:
                    case 3:
                    case 4:
                        SwitchButton.this.viewState.checkedLineColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(value, Integer.valueOf(SwitchButton.this.beforeState.checkedLineColor), Integer.valueOf(SwitchButton.this.afterState.checkedLineColor))).intValue();
                        SwitchButton.this.viewState.radius = SwitchButton.this.beforeState.radius + ((SwitchButton.this.afterState.radius - SwitchButton.this.beforeState.radius) * value);
                        if (SwitchButton.this.animateState != 1) {
                            SwitchButton.this.viewState.buttonX = SwitchButton.this.beforeState.buttonX + ((SwitchButton.this.afterState.buttonX - SwitchButton.this.beforeState.buttonX) * value);
                        }
                        SwitchButton.this.viewState.checkStateColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(value, Integer.valueOf(SwitchButton.this.beforeState.checkStateColor), Integer.valueOf(SwitchButton.this.afterState.checkStateColor))).intValue();
                        break;
                    case 5:
                        SwitchButton.this.viewState.buttonX = SwitchButton.this.beforeState.buttonX + ((SwitchButton.this.afterState.buttonX - SwitchButton.this.beforeState.buttonX) * value);
                        float fraction = (SwitchButton.this.viewState.buttonX - SwitchButton.this.buttonMinX) / (SwitchButton.this.buttonMaxX - SwitchButton.this.buttonMinX);
                        SwitchButton.this.viewState.checkStateColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(fraction, Integer.valueOf(SwitchButton.this.uncheckColor), Integer.valueOf(SwitchButton.this.checkedColor))).intValue();
                        SwitchButton.this.viewState.radius = fraction * SwitchButton.this.viewRadius;
                        SwitchButton.this.viewState.checkedLineColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(fraction, 0, Integer.valueOf(SwitchButton.this.checkLineColor))).intValue();
                        break;
                }
                SwitchButton.this.postInvalidate();
            }
        };
        this.animatorListener = new Animator.AnimatorListener() { // from class: androidx.nemosofts.driving.exam.view.SwitchButton.2
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                switch (SwitchButton.this.animateState) {
                    case 0 /* 0 */:
                    case 2:
                    default:
                        return;
                    case 1:
                        SwitchButton.this.animateState = 2;
                        SwitchButton.this.viewState.checkedLineColor = 0;
                        SwitchButton.this.viewState.radius = SwitchButton.this.viewRadius;
                        SwitchButton.this.postInvalidate();
                        return;
                    case 3:
                        SwitchButton.this.animateState = 0;
                        SwitchButton.this.postInvalidate();
                        return;
                    case 4:
                        SwitchButton.this.animateState = 0;
                        SwitchButton.this.postInvalidate();
                        SwitchButton.this.broadcastEvent();
                        return;
                    case 5:
                        SwitchButton.this.isChecked = !SwitchButton.this.isChecked;
                        SwitchButton.this.animateState = 0;
                        SwitchButton.this.postInvalidate();
                        SwitchButton.this.broadcastEvent();
                        return;
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animation) {
            }
        };
        init(context, attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.ANIMATE_STATE_NONE = 0;
        this.ANIMATE_STATE_PENDING_DRAG = 1;
        this.ANIMATE_STATE_DRAGING = 2;
        this.ANIMATE_STATE_PENDING_RESET = 3;
        this.ANIMATE_STATE_PENDING_SETTLE = 4;
        this.ANIMATE_STATE_SWITCH = 5;
        this.rect = new RectF();
        this.animateState = 0;
        this.argbEvaluator = new ArgbEvaluator();
        this.isTouchingDown = false;
        this.isUiInited = false;
        this.isEventBroadcast = false;
        this.postPendingDrag = () -> {
            if (!isInAnimating()) {
                pendingDragState();
            }
        };
        this.animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.nemosofts.driving.exam.view.SwitchButton.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
                switch (SwitchButton.this.animateState) {
                    case 1:
                    case 3:
                    case 4:
                        SwitchButton.this.viewState.checkedLineColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(value, Integer.valueOf(SwitchButton.this.beforeState.checkedLineColor), Integer.valueOf(SwitchButton.this.afterState.checkedLineColor))).intValue();
                        SwitchButton.this.viewState.radius = SwitchButton.this.beforeState.radius + ((SwitchButton.this.afterState.radius - SwitchButton.this.beforeState.radius) * value);
                        if (SwitchButton.this.animateState != 1) {
                            SwitchButton.this.viewState.buttonX = SwitchButton.this.beforeState.buttonX + ((SwitchButton.this.afterState.buttonX - SwitchButton.this.beforeState.buttonX) * value);
                        }
                        SwitchButton.this.viewState.checkStateColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(value, Integer.valueOf(SwitchButton.this.beforeState.checkStateColor), Integer.valueOf(SwitchButton.this.afterState.checkStateColor))).intValue();
                        break;
                    case 5:
                        SwitchButton.this.viewState.buttonX = SwitchButton.this.beforeState.buttonX + ((SwitchButton.this.afterState.buttonX - SwitchButton.this.beforeState.buttonX) * value);
                        float fraction = (SwitchButton.this.viewState.buttonX - SwitchButton.this.buttonMinX) / (SwitchButton.this.buttonMaxX - SwitchButton.this.buttonMinX);
                        SwitchButton.this.viewState.checkStateColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(fraction, Integer.valueOf(SwitchButton.this.uncheckColor), Integer.valueOf(SwitchButton.this.checkedColor))).intValue();
                        SwitchButton.this.viewState.radius = fraction * SwitchButton.this.viewRadius;
                        SwitchButton.this.viewState.checkedLineColor = ((Integer) SwitchButton.this.argbEvaluator.evaluate(fraction, 0, Integer.valueOf(SwitchButton.this.checkLineColor))).intValue();
                        break;
                }
                SwitchButton.this.postInvalidate();
            }
        };
        this.animatorListener = new Animator.AnimatorListener() { // from class: androidx.nemosofts.driving.exam.view.SwitchButton.2
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                switch (SwitchButton.this.animateState) {
                    case 1 /* 0 */:
                    case 2:
                    default:
                        return;
                    case 0:
                        SwitchButton.this.animateState = 2;
                        SwitchButton.this.viewState.checkedLineColor = 0;
                        SwitchButton.this.viewState.radius = SwitchButton.this.viewRadius;
                        SwitchButton.this.postInvalidate();
                        return;
                    case 3:
                        SwitchButton.this.animateState = 0;
                        SwitchButton.this.postInvalidate();
                        return;
                    case 4:
                        SwitchButton.this.animateState = 0;
                        SwitchButton.this.postInvalidate();
                        SwitchButton.this.broadcastEvent();
                        return;
                    case 5:
                        SwitchButton.this.isChecked = !SwitchButton.this.isChecked;
                        SwitchButton.this.animateState = 0;
                        SwitchButton.this.postInvalidate();
                        SwitchButton.this.broadcastEvent();
                        return;
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animation) {
            }
        };
        init(context, attrs);
    }

    @Override // android.view.View
    public final void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(0, 0, 0, 0);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = null;
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        }
        this.shadowEffect = optBoolean(typedArray, R.styleable.SwitchButton_sb_shadow_effect, true);
        this.uncheckCircleColor = optColor(typedArray, R.styleable.SwitchButton_sb_uncheckcircle_color, -5592406);
        this.uncheckCircleWidth = optPixelSize(typedArray, R.styleable.SwitchButton_sb_uncheckcircle_width, dp2pxInt(1.5f));
        this.uncheckCircleOffsetX = dp2px(10.0f);
        this.uncheckCircleRadius = optPixelSize(typedArray, R.styleable.SwitchButton_sb_uncheckcircle_radius, dp2px(4.0f));
        this.checkedLineOffsetX = dp2px(4.0f);
        this.checkedLineOffsetY = dp2px(4.0f);
        this.shadowRadius = optPixelSize(typedArray, R.styleable.SwitchButton_sb_shadow_radius, dp2pxInt(2.5f));
        this.shadowOffset = optPixelSize(typedArray, R.styleable.SwitchButton_sb_shadow_offset, dp2pxInt(1.5f));
        this.shadowColor = optColor(typedArray, R.styleable.SwitchButton_sb_shadow_color, 855638016);
        this.uncheckColor = optColor(typedArray, R.styleable.SwitchButton_sb_uncheck_color, -2236963);
        this.checkedColor = optColor(typedArray, R.styleable.SwitchButton_sb_checked_color, -11414681);
        this.borderWidth = optPixelSize(typedArray, R.styleable.SwitchButton_sb_border_width, dp2pxInt(1.0f));
        this.checkLineColor = optColor(typedArray, R.styleable.SwitchButton_sb_checkline_color, -1);
        this.checkLineWidth = optPixelSize(typedArray, R.styleable.SwitchButton_sb_checkline_width, dp2pxInt(1.0f));
        this.checkLineLength = dp2px(6.0f);
        int buttonColor = optColor(typedArray, R.styleable.SwitchButton_sb_button_color, -1);
        this.uncheckButtonColor = optColor(typedArray, R.styleable.SwitchButton_sb_uncheckbutton_color, buttonColor);
        this.checkedButtonColor = optColor(typedArray, R.styleable.SwitchButton_sb_checkedbutton_color, buttonColor);
        int effectDuration = optInt(typedArray, R.styleable.SwitchButton_sb_effect_duration, 300);
        this.isChecked = optBoolean(typedArray, R.styleable.SwitchButton_sb_checked, false);
        this.showIndicator = optBoolean(typedArray, R.styleable.SwitchButton_sb_show_indicator, true);
        this.background = optColor(typedArray, R.styleable.SwitchButton_sb_background, -1);
        this.enableEffect = optBoolean(typedArray, R.styleable.SwitchButton_sb_enable_effect, true);
        if (typedArray != null) {
            typedArray.recycle();
        }
        this.paint = new Paint(1);
        this.buttonPaint = new Paint(1);
        this.buttonPaint.setColor(buttonColor);
        if (this.shadowEffect) {
            this.buttonPaint.setShadowLayer(this.shadowRadius, 0.0f, this.shadowOffset, this.shadowColor);
        }
        this.viewState = new ViewState();
        this.beforeState = new ViewState();
        this.afterState = new ViewState();
        this.valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.valueAnimator.setDuration(effectDuration);
        this.valueAnimator.setRepeatCount(0);
        this.valueAnimator.addUpdateListener(this.animatorUpdateListener);
        this.valueAnimator.addListener(this.animatorListener);
        super.setClickable(true);
        setPadding(0, 0, 0, 0);
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(1, null);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == 0 || widthMode == Integer.MIN_VALUE) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(DEFAULT_WIDTH, 1073741824);
        }
        if (heightMode == 0 || heightMode == Integer.MIN_VALUE) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(DEFAULT_HEIGHT, 1073741824);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float viewPadding = Math.max(this.shadowRadius + this.shadowOffset, this.borderWidth);
        this.height = (h - viewPadding) - viewPadding;
        this.width = (w - viewPadding) - viewPadding;
        this.viewRadius = this.height * 0.5f;
        this.buttonRadius = this.viewRadius - this.borderWidth;
        this.left = viewPadding;
        this.top = viewPadding;
        this.right = w - viewPadding;
        this.bottom = h - viewPadding;
        this.centerX = (this.left + this.right) * 0.5f;
        this.centerY = (this.top + this.bottom) * 0.5f;
        this.buttonMinX = this.left + this.viewRadius;
        this.buttonMaxX = this.right - this.viewRadius;
        if (isChecked()) {
            setCheckedViewState(this.viewState);
        } else {
            setUncheckViewState(this.viewState);
        }
        this.isUiInited = true;
        postInvalidate();
    }

    private void setUncheckViewState(@NonNull ViewState viewState) {
        viewState.radius = 0.0f;
        viewState.checkStateColor = this.uncheckColor;
        viewState.checkedLineColor = 0;
        viewState.buttonX = this.buttonMinX;
        this.buttonPaint.setColor(this.uncheckButtonColor);
    }

    private void setCheckedViewState(@NonNull ViewState viewState) {
        viewState.radius = this.viewRadius;
        viewState.checkStateColor = this.checkedColor;
        viewState.checkedLineColor = this.checkLineColor;
        viewState.buttonX = this.buttonMaxX;
        this.buttonPaint.setColor(this.checkedButtonColor);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.setStrokeWidth(this.borderWidth);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(this.background);
        drawRoundRect(canvas, this.left, this.top, this.right, this.bottom, this.viewRadius, this.paint);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(this.uncheckColor);
        drawRoundRect(canvas, this.left, this.top, this.right, this.bottom, this.viewRadius, this.paint);
        if (this.showIndicator) {
            drawUncheckIndicator(canvas);
        }
        float des = this.viewState.radius * 0.5f;
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(this.viewState.checkStateColor);
        this.paint.setStrokeWidth(this.borderWidth + (des * 2.0f));
        drawRoundRect(canvas, this.left + des, this.top + des, this.right - des, this.bottom - des, this.viewRadius, this.paint);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setStrokeWidth(1.0f);
        drawArc(canvas, this.left, this.top, this.left + (2.0f * this.viewRadius), this.top + (2.0f * this.viewRadius), 90.0f, 180.0f, this.paint);
        canvas.drawRect(this.left + this.viewRadius, this.top, this.viewState.buttonX, this.top + (2.0f * this.viewRadius), this.paint);
        if (this.showIndicator) {
            drawCheckedIndicator(canvas);
        }
        drawButton(canvas, this.viewState.buttonX, this.centerY);
    }

    protected void drawCheckedIndicator(Canvas canvas) {
        drawCheckedIndicator(canvas, this.viewState.checkedLineColor, this.checkLineWidth, (this.left + this.viewRadius) - this.checkedLineOffsetX, this.centerY - this.checkLineLength, (this.left + this.viewRadius) - this.checkedLineOffsetY, this.centerY + this.checkLineLength, this.paint);
    }

    protected void drawCheckedIndicator(Canvas canvas, int color, float lineWidth, float sx, float sy, float ex, float ey, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(lineWidth);
        canvas.drawLine(sx, sy, ex, ey, paint);
    }

    private void drawUncheckIndicator(Canvas canvas) {
        drawUncheckIndicator(canvas, this.uncheckCircleColor, this.uncheckCircleWidth, this.right - this.uncheckCircleOffsetX, this.centerY, this.uncheckCircleRadius, this.paint);
    }

    protected void drawUncheckIndicator(@NonNull Canvas canvas, int color, float lineWidth, float centerX, float centerY, float radius, @NonNull Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(lineWidth);
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    private void drawArc(Canvas canvas, float left, float top, float right, float bottom, float startAngle, float sweepAngle, Paint paint) {
        if (Build.VERSION.SDK_INT >= 21) {
            canvas.drawArc(left, top, right, bottom, startAngle, sweepAngle, true, paint);
            return;
        }
        this.rect.set(left, top, right, bottom);
        canvas.drawArc(this.rect, startAngle, sweepAngle, true, paint);
    }

    private void drawRoundRect(Canvas canvas, float left, float top, float right, float bottom, float backgroundRadius, Paint paint) {
        if (Build.VERSION.SDK_INT >= 21) {
            canvas.drawRoundRect(left, top, right, bottom, backgroundRadius, backgroundRadius, paint);
            return;
        }
        this.rect.set(left, top, right, bottom);
        canvas.drawRoundRect(this.rect, backgroundRadius, backgroundRadius, paint);
    }

    private void drawButton(@NonNull Canvas canvas, float x, float y) {
        canvas.drawCircle(x, y, this.buttonRadius, this.buttonPaint);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(1.0f);
        this.paint.setColor(-2236963);
        canvas.drawCircle(x, y, this.buttonRadius, this.paint);
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean checked) {
        if (checked == isChecked()) {
            postInvalidate();
        } else {
            toggle(this.enableEffect, false);
        }
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return this.isChecked;
    }

    @Override // android.widget.Checkable
    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean animate) {
        toggle(animate, true);
    }

    private void toggle(boolean animate, boolean broadcast) {
        if (isEnabled()) {
            if (this.isEventBroadcast) {
                throw new RuntimeException("should NOT switch the state in method: [onCheckedChanged]!");
            }
            if (!this.isUiInited) {
                this.isChecked = !this.isChecked;
                if (broadcast) {
                    broadcastEvent();
                    return;
                }
                return;
            }
            if (this.valueAnimator.isRunning()) {
                this.valueAnimator.cancel();
            }
            if (!this.enableEffect || !animate) {
                this.isChecked = !this.isChecked;
                if (isChecked()) {
                    setCheckedViewState(this.viewState);
                } else {
                    setUncheckViewState(this.viewState);
                }
                postInvalidate();
                if (broadcast) {
                    broadcastEvent();
                    return;
                }
                return;
            }
            this.animateState = 5;
            this.beforeState.copy(this.viewState);
            if (isChecked()) {
                setUncheckViewState(this.afterState);
            } else {
                setCheckedViewState(this.afterState);
            }
            this.valueAnimator.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastEvent() {
        if (this.onCheckedChangeListener != null) {
            this.isEventBroadcast = true;
            this.onCheckedChangeListener.onCheckedChanged(this, isChecked());
        }
        this.isEventBroadcast = false;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            int actionMasked = event.getActionMasked();
            switch (actionMasked) {
                case 1 /* 0 */:
                    this.isTouchingDown = true;
                    this.touchDownTime = System.currentTimeMillis();
                    removeCallbacks(this.postPendingDrag);
                    postDelayed(this.postPendingDrag, 100L);
                    return true;
                case 0:
                    this.isTouchingDown = false;
                    removeCallbacks(this.postPendingDrag);
                    if (System.currentTimeMillis() - this.touchDownTime <= 300) {
                        toggle();
                        return true;
                    } else if (!isDragState()) {
                        if (isPendingDragState()) {
                            pendingCancelDragState();
                            return true;
                        }
                        return true;
                    } else {
                        float fraction = event.getX() / getWidth();
                        boolean newCheck = Math.max(0.0f, Math.min(1.0f, fraction)) > 0.5f;
                        if (newCheck == isChecked()) {
                            pendingCancelDragState();
                            return true;
                        }
                        this.isChecked = newCheck;
                        pendingSettleState();
                        return true;
                    }
                case 2:
                    float eventX = event.getX();
                    if (isPendingDragState()) {
                        float fraction2 = eventX / getWidth();
                        this.viewState.buttonX = this.buttonMinX + ((this.buttonMaxX - this.buttonMinX) * Math.max(0.0f, Math.min(1.0f, fraction2)));
                        return true;
                    } else if (isDragState()) {
                        float fraction3 = eventX / getWidth();
                        float fraction4 = Math.max(0.0f, Math.min(1.0f, fraction3));
                        this.viewState.buttonX = this.buttonMinX + ((this.buttonMaxX - this.buttonMinX) * fraction4);
                        this.viewState.checkStateColor = ((Integer) this.argbEvaluator.evaluate(fraction4, Integer.valueOf(this.uncheckColor), Integer.valueOf(this.checkedColor))).intValue();
                        postInvalidate();
                        return true;
                    } else {
                        return true;
                    }
                case 3:
                    this.isTouchingDown = false;
                    removeCallbacks(this.postPendingDrag);
                    if (isPendingDragState() || isDragState()) {
                        pendingCancelDragState();
                        return true;
                    }
                    return true;
                default:
                    return true;
            }
        }
        return false;
    }

    private boolean isInAnimating() {
        return this.animateState != 0;
    }

    private boolean isPendingDragState() {
        return this.animateState == 1 || this.animateState == 3;
    }

    private boolean isDragState() {
        return this.animateState == 2;
    }

    public void setShadowEffect(boolean shadowEffect) {
        if (this.shadowEffect == shadowEffect) {
            return;
        }
        this.shadowEffect = shadowEffect;
        if (this.shadowEffect) {
            this.buttonPaint.setShadowLayer(this.shadowRadius, 0.0f, this.shadowOffset, this.shadowColor);
        } else {
            this.buttonPaint.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
        }
    }

    public void setEnableEffect(boolean enable) {
        this.enableEffect = enable;
    }

    private void pendingDragState() {
        if (!isInAnimating() && this.isTouchingDown) {
            if (this.valueAnimator.isRunning()) {
                this.valueAnimator.cancel();
            }
            this.animateState = 1;
            this.beforeState.copy(this.viewState);
            this.afterState.copy(this.viewState);
            if (isChecked()) {
                this.afterState.checkStateColor = this.checkedColor;
                this.afterState.buttonX = this.buttonMaxX;
                this.afterState.checkedLineColor = this.checkedColor;
            } else {
                this.afterState.checkStateColor = this.uncheckColor;
                this.afterState.buttonX = this.buttonMinX;
                this.afterState.radius = this.viewRadius;
            }
            this.valueAnimator.start();
        }
    }

    private void pendingCancelDragState() {
        if (isDragState() || isPendingDragState()) {
            if (this.valueAnimator.isRunning()) {
                this.valueAnimator.cancel();
            }
            this.animateState = 3;
            this.beforeState.copy(this.viewState);
            if (isChecked()) {
                setCheckedViewState(this.afterState);
            } else {
                setUncheckViewState(this.afterState);
            }
            this.valueAnimator.start();
        }
    }

    private void pendingSettleState() {
        if (this.valueAnimator.isRunning()) {
            this.valueAnimator.cancel();
        }
        this.animateState = 4;
        this.beforeState.copy(this.viewState);
        if (isChecked()) {
            setCheckedViewState(this.afterState);
        } else {
            setUncheckViewState(this.afterState);
        }
        this.valueAnimator.start();
    }

    @Override // android.view.View
    public final void setOnClickListener(View.OnClickListener l) {
    }

    @Override // android.view.View
    public final void setOnLongClickListener(View.OnLongClickListener l) {
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener l) {
        this.onCheckedChangeListener = l;
    }

    private static float dp2px(float dp) {
        Resources r = Resources.getSystem();
        return TypedValue.applyDimension(1, dp, r.getDisplayMetrics());
    }

    private static int dp2pxInt(float dp) {
        return (int) dp2px(dp);
    }

    private static int optInt(TypedArray typedArray, int index, int def) {
        return typedArray == null ? def : typedArray.getInt(index, def);
    }

    private static float optPixelSize(TypedArray typedArray, int index, float def) {
        return typedArray == null ? def : typedArray.getDimension(index, def);
    }

    private static int optPixelSize(TypedArray typedArray, int index, int def) {
        return typedArray == null ? def : typedArray.getDimensionPixelOffset(index, def);
    }

    private static int optColor(TypedArray typedArray, int index, int def) {
        return typedArray == null ? def : typedArray.getColor(index, def);
    }

    private static boolean optBoolean(TypedArray typedArray, int index, boolean def) {
        return typedArray == null ? def : typedArray.getBoolean(index, def);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/view/SwitchButton$ViewState.class */
    public static class ViewState {
        float buttonX;
        int checkStateColor;
        int checkedLineColor;
        float radius;

        ViewState() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void copy(@NonNull ViewState source) {
            this.buttonX = source.buttonX;
            this.checkStateColor = source.checkStateColor;
            this.checkedLineColor = source.checkedLineColor;
            this.radius = source.radius;
        }
    }
}
