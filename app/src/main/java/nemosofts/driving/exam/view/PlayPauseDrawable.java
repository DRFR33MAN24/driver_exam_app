package nemosofts.driving.exam.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Property;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import org.jetbrains.annotations.Contract;

/* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/view/PlayPauseDrawable.class */
public class PlayPauseDrawable extends Drawable {
    private static final Property<PlayPauseDrawable, Float> PROGRESS = new Property<PlayPauseDrawable, Float>(Float.class, "progress") { // from class: androidx.nemosofts.driving.exam.view.PlayPauseDrawable.1
        @Override // android.util.Property
        @NonNull
        @Contract(pure = true)
        public Float get(@NonNull PlayPauseDrawable d) {
            return Float.valueOf(d.getProgress());
        }

        @Override // android.util.Property
        public void set(@NonNull PlayPauseDrawable d, Float value) {
            d.setProgress(value.floatValue());
        }
    };
    private float mPauseBarWidth;
    private float mPauseBarHeight;
    private float mPauseBarDistance;
    private float mWidth;
    private float mHeight;
    private final Path mLeftPauseBar = new Path();
    private final Path mRightPauseBar = new Path();
    private final Paint mPaint = new Paint();
    private final RectF mBounds = new RectF();
    private float mProgress = 1.0f;
    private boolean mIsPlay = true;

    public PlayPauseDrawable(@ColorInt int fillColor) {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(fillColor);
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.mBounds.set(bounds);
        this.mWidth = this.mBounds.width();
        this.mHeight = this.mBounds.height();
        this.mPauseBarHeight = this.mHeight / 2.5f;
        this.mPauseBarWidth = this.mPauseBarHeight / 2.5f;
        this.mPauseBarDistance = this.mPauseBarWidth / 1.5f;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(@NonNull Canvas canvas) {
        this.mLeftPauseBar.rewind();
        this.mRightPauseBar.rewind();
        float barDist = lerp(this.mPauseBarDistance, 0.0f, this.mProgress) - 1.0f;
        float barWidth = lerp(this.mPauseBarWidth, this.mPauseBarHeight / 2.0f, this.mProgress);
        float firstBarTopLeft = lerp(0.0f, barWidth, this.mProgress);
        float secondBarTopRight = lerp((2.0f * barWidth) + barDist, barWidth + barDist, this.mProgress);
        this.mLeftPauseBar.moveTo(0.0f, 0.0f);
        this.mLeftPauseBar.lineTo(firstBarTopLeft, -this.mPauseBarHeight);
        this.mLeftPauseBar.lineTo(barWidth, -this.mPauseBarHeight);
        this.mLeftPauseBar.lineTo(barWidth, 0.0f);
        this.mLeftPauseBar.close();
        this.mRightPauseBar.moveTo(barWidth + barDist, 0.0f);
        this.mRightPauseBar.lineTo(barWidth + barDist, -this.mPauseBarHeight);
        this.mRightPauseBar.lineTo(secondBarTopRight, -this.mPauseBarHeight);
        this.mRightPauseBar.lineTo((2.0f * barWidth) + barDist, 0.0f);
        this.mRightPauseBar.close();
        canvas.save();
        canvas.translate(lerp(0.0f, this.mPauseBarHeight / 8.0f, this.mProgress), 0.0f);
        float rotationProgress = this.mIsPlay ? 1.0f - this.mProgress : this.mProgress;
        float startingRotation = this.mIsPlay ? 90.0f : 0.0f;
        canvas.rotate(lerp(startingRotation, startingRotation + 90.0f, rotationProgress), this.mWidth / 2.0f, this.mHeight / 2.0f);
        canvas.translate((this.mWidth / 2.0f) - (((2.0f * barWidth) + barDist) / 2.0f), (this.mHeight / 2.0f) + (this.mPauseBarHeight / 2.0f));
        canvas.drawPath(this.mLeftPauseBar, this.mPaint);
        canvas.drawPath(this.mRightPauseBar, this.mPaint);
        canvas.restore();
    }

    public void setPlay() {
        this.mIsPlay = true;
        this.mProgress = 1.0f;
    }

    public void setPause() {
        this.mIsPlay = false;
        this.mProgress = 0.0f;
    }

    public Animator getPausePlayAnimator() {
        Property<PlayPauseDrawable, Float> property = PROGRESS;
        float[] fArr = new float[2];
        fArr[0] = this.mIsPlay ? 1.0f : 0.0f;
        fArr[1] = this.mIsPlay ? 0.0f : 1.0f;
        Animator anim = ObjectAnimator.ofFloat(this, property, fArr);
        anim.addListener(new AnimatorListenerAdapter() { // from class: androidx.nemosofts.driving.exam.view.PlayPauseDrawable.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
                PlayPauseDrawable.this.mIsPlay = !PlayPauseDrawable.this.mIsPlay;
            }
        });
        return anim;
    }

    public boolean isPlay() {
        return this.mIsPlay;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getProgress() {
        return this.mProgress;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    private static float lerp(float a, float b, float t) {
        return a + ((b - a) * t);
    }
}
