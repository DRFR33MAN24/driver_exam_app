package nemosofts.driving.exam.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import nemosofts.driving.exam.R;
import org.jetbrains.annotations.Contract;

/* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/view/PlayPauseView.class */
public class PlayPauseView extends FrameLayout {
    private static final Property<PlayPauseView, Integer> COLOR = new Property<PlayPauseView, Integer>(Integer.class, "color") { // from class: androidx.nemosofts.driving.exam.view.PlayPauseView.1
        @Override // android.util.Property
        @NonNull
        public Integer get(@NonNull PlayPauseView v) {
            return Integer.valueOf(v.getColor());
        }

        @Override // android.util.Property
        public void set(@NonNull PlayPauseView v, Integer value) {
            v.setColor(value.intValue());
        }
    };
    private static final long PLAY_PAUSE_ANIMATION_DURATION = 200;
    private final PlayPauseDrawable mDrawable;
    private final Paint mPaint;
    private final int mPauseBackgroundColor;
    private final int mPlayBackgroundColor;
    private AnimatorSet mAnimatorSet;
    private int mBackgroundColor;
    private int mWidth;
    private int mHeight;

    public PlayPauseView(Context context) {
        super(context);
        this.mPaint = new Paint();
        this.mPlayBackgroundColor = -16776961;
        this.mPauseBackgroundColor = -16711681;
        this.mDrawable = new PlayPauseDrawable(-1);
        init();
    }

    public PlayPauseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mPaint = new Paint();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PlayPauseView, 0, 0);
        try {
            this.mPlayBackgroundColor = a.getColor(R.styleable.PlayPauseView_play_bg, -16776961);
            this.mPauseBackgroundColor = a.getColor(R.styleable.PlayPauseView_pause_bg, -16711681);
            int fillColor = a.getColor(R.styleable.PlayPauseView_fill_color, -1);
            a.recycle();
            this.mDrawable = new PlayPauseDrawable(fillColor);
            init();
        } catch (Throwable th) {
            a.recycle();
            throw th;
        }
    }

    private void init() {
        setWillNotDraw(false);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mDrawable.setCallback(this);
        this.mBackgroundColor = this.mPlayBackgroundColor;
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.isPlay = this.mDrawable.isPlay();
        return ss;
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        initStatus(ss.isPlay);
        invalidate();
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);
        setMeasuredDimension(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size, 1073741824));
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mDrawable.setBounds(0, 0, w, h);
        this.mWidth = w;
        this.mHeight = h;
        if (Build.VERSION.SDK_INT >= 21) {
            setOutlineProvider(new ViewOutlineProvider() { // from class: androidx.nemosofts.driving.exam.view.PlayPauseView.2
                @Override // android.view.ViewOutlineProvider
                @TargetApi(21)
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                }
            });
            setClipToOutline(true);
        }
    }

    public void setColor(int color) {
        this.mBackgroundColor = color;
        invalidate();
    }

    public int getColor() {
        return this.mBackgroundColor;
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable who) {
        return who == this.mDrawable || super.verifyDrawable(who);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint.setColor(this.mBackgroundColor);
        float radius = Math.min(this.mWidth, this.mHeight) / 2.0f;
        canvas.drawCircle(this.mWidth / 2.0f, this.mHeight / 2.0f, radius, this.mPaint);
        this.mDrawable.draw(canvas);
    }

    public void toggle() {
        toggle(true);
    }

    public void change(boolean isPlay) {
        change(isPlay, true);
    }

    public void change(boolean isPlay, boolean withAnim) {
        if (this.mDrawable.isPlay() == isPlay) {
            return;
        }
        toggle(withAnim);
    }

    public void toggle(boolean withAnim) {
        if (withAnim) {
            if (this.mAnimatorSet != null) {
                this.mAnimatorSet.cancel();
            }
            this.mAnimatorSet = new AnimatorSet();
            boolean isPlay = this.mDrawable.isPlay();
            Property<PlayPauseView, Integer> property = COLOR;
            int[] iArr = new int[1];
            iArr[0] = isPlay ? this.mPauseBackgroundColor : this.mPlayBackgroundColor;
            ObjectAnimator colorAnim = ObjectAnimator.ofInt(this, property, iArr);
            colorAnim.setEvaluator(new ArgbEvaluator());
            Animator pausePlayAnim = this.mDrawable.getPausePlayAnimator();
            this.mAnimatorSet.setInterpolator(new DecelerateInterpolator());
            this.mAnimatorSet.setDuration(PLAY_PAUSE_ANIMATION_DURATION);
            this.mAnimatorSet.playTogether(colorAnim, pausePlayAnim);
            this.mAnimatorSet.start();
            return;
        }
        boolean isPlay2 = this.mDrawable.isPlay();
        initStatus(!isPlay2);
        invalidate();
    }

    private void initStatus(boolean isPlay) {
        if (isPlay) {
            this.mDrawable.setPlay();
            setColor(this.mPlayBackgroundColor);
            return;
        }
        this.mDrawable.setPause();
        setColor(this.mPauseBackgroundColor);
    }

    public boolean isPlay() {
        return this.mDrawable.isPlay();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/view/PlayPauseView$SavedState.class */
    public static class SavedState extends View.BaseSavedState {
        boolean isPlay;
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: androidx.nemosofts.driving.exam.view.PlayPauseView.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            @NonNull
            @Contract("_ -> new")
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            @NonNull
            @Contract(value = "_ -> new", pure = true)
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.isPlay = in.readByte() > 0;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte(this.isPlay ? (byte) 1 : (byte) 0);
        }
    }
}
