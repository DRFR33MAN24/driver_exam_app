package nemosofts.driving.exam.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.utils.NemosoftsUtil;

/* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/view/ToggleView.class */
public class ToggleView extends RelativeLayout {
    private static final int DEFAULT_ANIM_DURATION = 300;
    private ToggleItem bubbleToggleItem;
    private boolean isActive;
    private ImageView iconView;
    private TextView titleView;
    private TextView badgeView;
    private int animationDuration;
    private boolean showShapeAlways;
    private float maxTitleWidth;
    private float measuredTitleWidth;

    public ToggleView(Context context) {
        super(context);
        this.isActive = false;
        init(context, null);
    }

    public ToggleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.isActive = false;
        init(context, attrs);
    }

    public ToggleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.isActive = false;
        init(context, attrs);
    }

    public ToggleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.isActive = false;
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        String title = "Title";
        Drawable icon = null;
        Drawable shape = null;
        int shapeColor = Integer.MIN_VALUE;
        int colorActive = NemosoftsUtil.getThemeAccentColor(context);
        int colorInactive = ContextCompat.getColor(context, R.color.default_inactive_color);
        float titleSize = context.getResources().getDimension(R.dimen.default_nav_item_text_size);
        this.maxTitleWidth = context.getResources().getDimension(R.dimen.default_nav_item_title_max_width);
        float iconWidth = context.getResources().getDimension(R.dimen.default_icon_size);
        float iconHeight = context.getResources().getDimension(R.dimen.default_icon_size);
        int internalPadding = (int) context.getResources().getDimension(R.dimen.default_nav_item_padding);
        int titlePadding = (int) context.getResources().getDimension(R.dimen.default_nav_item_text_padding);
        int badgeTextSize = (int) context.getResources().getDimension(R.dimen.default_nav_item_badge_text_size);
        int badgeBackgroundColor = ContextCompat.getColor(context, R.color.default_badge_background_color);
        int badgeTextColor = ContextCompat.getColor(context, R.color.default_badge_text_color);
        String badgeText = null;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BubbleToggleView, 0, 0);
            try {
                if (Build.VERSION.SDK_INT >= 21) {
                    icon = ta.getDrawable(R.styleable.BubbleToggleView_bt_icon);
                } else {
                    icon = AppCompatResources.getDrawable(getContext(), ta.getResourceId(R.styleable.BubbleToggleView_bt_icon, R.drawable.default_icon));
                }
                iconWidth = ta.getDimension(R.styleable.BubbleToggleView_bt_iconWidth, iconWidth);
                iconHeight = ta.getDimension(R.styleable.BubbleToggleView_bt_iconHeight, iconHeight);
                shape = ta.getDrawable(R.styleable.BubbleToggleView_bt_shape);
                shapeColor = ta.getColor(R.styleable.BubbleToggleView_bt_shapeColor, Integer.MIN_VALUE);
                this.showShapeAlways = ta.getBoolean(R.styleable.BubbleToggleView_bt_showShapeAlways, false);
                title = ta.getString(R.styleable.BubbleToggleView_bt_title);
                titleSize = ta.getDimension(R.styleable.BubbleToggleView_bt_titleSize, titleSize);
                colorActive = ta.getColor(R.styleable.BubbleToggleView_bt_colorActive, colorActive);
                colorInactive = ta.getColor(R.styleable.BubbleToggleView_bt_colorInactive, colorInactive);
                this.isActive = ta.getBoolean(R.styleable.BubbleToggleView_bt_active, false);
                this.animationDuration = ta.getInteger(R.styleable.BubbleToggleView_bt_duration, DEFAULT_ANIM_DURATION);
                internalPadding = (int) ta.getDimension(R.styleable.BubbleToggleView_bt_padding, internalPadding);
                titlePadding = (int) ta.getDimension(R.styleable.BubbleToggleView_bt_titlePadding, titlePadding);
                badgeTextSize = (int) ta.getDimension(R.styleable.BubbleToggleView_bt_badgeTextSize, badgeTextSize);
                badgeBackgroundColor = ta.getColor(R.styleable.BubbleToggleView_bt_badgeBackgroundColor, badgeBackgroundColor);
                badgeTextColor = ta.getColor(R.styleable.BubbleToggleView_bt_badgeTextColor, badgeTextColor);
                badgeText = ta.getString(R.styleable.BubbleToggleView_bt_badgeText);
                ta.recycle();
            } catch (Throwable th) {
                ta.recycle();
                throw th;
            }
        }
        if (icon == null) {
            icon = ContextCompat.getDrawable(context, R.drawable.default_icon);
        }
        if (shape == null) {
            shape = ContextCompat.getDrawable(context, R.drawable.default_transition_drawable);
        }
        this.bubbleToggleItem = new ToggleItem();
        this.bubbleToggleItem.setIcon(icon);
        this.bubbleToggleItem.setShape(shape);
        this.bubbleToggleItem.setTitle(title);
        this.bubbleToggleItem.setTitleSize(titleSize);
        this.bubbleToggleItem.setTitlePadding(titlePadding);
        this.bubbleToggleItem.setShapeColor(shapeColor);
        this.bubbleToggleItem.setColorActive(colorActive);
        this.bubbleToggleItem.setColorInactive(colorInactive);
        this.bubbleToggleItem.setIconWidth(iconWidth);
        this.bubbleToggleItem.setIconHeight(iconHeight);
        this.bubbleToggleItem.setInternalPadding(internalPadding);
        this.bubbleToggleItem.setBadgeText(badgeText);
        this.bubbleToggleItem.setBadgeBackgroundColor(badgeBackgroundColor);
        this.bubbleToggleItem.setBadgeTextColor(badgeTextColor);
        this.bubbleToggleItem.setBadgeTextSize(badgeTextSize);
        setGravity(17);
        setPadding(this.bubbleToggleItem.getInternalPadding(), this.bubbleToggleItem.getInternalPadding(), this.bubbleToggleItem.getInternalPadding(), this.bubbleToggleItem.getInternalPadding());
        post(() -> {
            setPadding(this.bubbleToggleItem.getInternalPadding(), this.bubbleToggleItem.getInternalPadding(), this.bubbleToggleItem.getInternalPadding(), this.bubbleToggleItem.getInternalPadding());
        });
        createBubbleItemView(context);
        setInitialState(this.isActive);
    }

    @SuppressLint({"ObsoleteSdkInt"})
    private void createBubbleItemView(Context context) {
        this.iconView = new ImageView(context);
        this.iconView.setId(ViewCompat.generateViewId());
        RelativeLayout.LayoutParams lpIcon = new RelativeLayout.LayoutParams((int) this.bubbleToggleItem.getIconWidth(), (int) this.bubbleToggleItem.getIconHeight());
        lpIcon.addRule(15, -1);
        this.iconView.setLayoutParams(lpIcon);
        this.iconView.setImageDrawable(this.bubbleToggleItem.getIcon());
        this.titleView = new TextView(context);
        RelativeLayout.LayoutParams lpTitle = new RelativeLayout.LayoutParams(-2, -2);
        lpTitle.addRule(15, -1);
        if (Build.VERSION.SDK_INT >= 17) {
            lpTitle.addRule(17, this.iconView.getId());
        } else {
            lpTitle.addRule(1, this.iconView.getId());
        }
        this.titleView.setLayoutParams(lpTitle);
        this.titleView.setSingleLine(true);
        this.titleView.setTextColor(this.bubbleToggleItem.getColorActive());
        this.titleView.setText(this.bubbleToggleItem.getTitle());
        this.titleView.setTextSize(0, this.bubbleToggleItem.getTitleSize());
        this.titleView.setVisibility(0);
        this.titleView.setPadding(this.bubbleToggleItem.getTitlePadding(), 0, this.bubbleToggleItem.getTitlePadding(), 0);
        this.titleView.measure(0, 0);
        this.measuredTitleWidth = this.titleView.getMeasuredWidth();
        if (this.measuredTitleWidth > this.maxTitleWidth) {
            this.measuredTitleWidth = this.maxTitleWidth;
        }
        this.titleView.setVisibility(8);
        addView(this.iconView);
        addView(this.titleView);
        updateBadge(context);
        setInitialState(this.isActive);
    }

    @SuppressLint({"ObsoleteSdkInt"})
    private void updateBadge(Context context) {
        if (this.badgeView != null) {
            removeView(this.badgeView);
        }
        if (this.bubbleToggleItem.getBadgeText() == null) {
            return;
        }
        this.badgeView = new TextView(context);
        RelativeLayout.LayoutParams lpBadge = new RelativeLayout.LayoutParams(-2, -2);
        lpBadge.addRule(6, this.iconView.getId());
        if (Build.VERSION.SDK_INT >= 17) {
            lpBadge.addRule(19, this.iconView.getId());
        } else {
            lpBadge.addRule(7, this.iconView.getId());
        }
        this.badgeView.setLayoutParams(lpBadge);
        this.badgeView.setSingleLine(true);
        this.badgeView.setTextColor(this.bubbleToggleItem.getBadgeTextColor());
        this.badgeView.setText(this.bubbleToggleItem.getBadgeText());
        this.badgeView.setTextSize(0, this.bubbleToggleItem.getBadgeTextSize());
        this.badgeView.setGravity(17);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.default_badge_bg);
        NemosoftsUtil.updateDrawableColor(drawable, this.bubbleToggleItem.getBadgeBackgroundColor());
        this.badgeView.setBackground(drawable);
        int badgePadding = (int) context.getResources().getDimension(R.dimen.default_nav_item_badge_padding);
        this.badgeView.setPadding(badgePadding, 0, badgePadding, 0);
        this.badgeView.measure(0, 0);
        if (this.badgeView.getMeasuredWidth() < this.badgeView.getMeasuredHeight()) {
            this.badgeView.setWidth(this.badgeView.getMeasuredHeight());
        }
        addView(this.badgeView);
    }

    public void setInitialState(boolean isActive) {
        setBackground(this.bubbleToggleItem.getShape());
        if (isActive) {
            NemosoftsUtil.updateDrawableColor(this.iconView.getDrawable(), this.bubbleToggleItem.getColorActive());
            this.isActive = true;
            this.titleView.setVisibility(0);
            if (getBackground() instanceof TransitionDrawable) {
                TransitionDrawable trans = (TransitionDrawable) getBackground();
                trans.startTransition(0);
                return;
            } else if (!this.showShapeAlways && this.bubbleToggleItem.getShapeColor() != Integer.MIN_VALUE) {
                NemosoftsUtil.updateDrawableColor(this.bubbleToggleItem.getShape(), this.bubbleToggleItem.getShapeColor());
                return;
            } else {
                return;
            }
        }
        NemosoftsUtil.updateDrawableColor(this.iconView.getDrawable(), this.bubbleToggleItem.getColorInactive());
        this.isActive = false;
        this.titleView.setVisibility(8);
        if (!this.showShapeAlways) {
            if (!(getBackground() instanceof TransitionDrawable)) {
                setBackground(null);
                return;
            }
            TransitionDrawable trans2 = (TransitionDrawable) getBackground();
            trans2.resetTransition();
        }
    }

    public void toggle() {
        if (!this.isActive) {
            activate();
        } else {
            deactivate();
        }
    }

    public void activate() {
        NemosoftsUtil.updateDrawableColor(this.iconView.getDrawable(), this.bubbleToggleItem.getColorActive());
        this.isActive = true;
        this.titleView.setVisibility(0);
        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setDuration(this.animationDuration);
        animator.addUpdateListener(animation -> {
            float value = ((Float) animation.getAnimatedValue()).floatValue();
            this.titleView.setWidth((int) (this.measuredTitleWidth * value));
            if (value >= 1.0f) {
            }
        });
        animator.start();
        if (getBackground() instanceof TransitionDrawable) {
            TransitionDrawable trans = (TransitionDrawable) getBackground();
            trans.startTransition(this.animationDuration);
            return;
        }
        if (!this.showShapeAlways && this.bubbleToggleItem.getShapeColor() != Integer.MIN_VALUE) {
            NemosoftsUtil.updateDrawableColor(this.bubbleToggleItem.getShape(), this.bubbleToggleItem.getShapeColor());
        }
        setBackground(this.bubbleToggleItem.getShape());
    }

    public void deactivate() {
        NemosoftsUtil.updateDrawableColor(this.iconView.getDrawable(), this.bubbleToggleItem.getColorInactive());
        this.isActive = false;
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.0f);
        animator.setDuration(this.animationDuration);
        animator.addUpdateListener(animation -> {
            float value = ((Float) animation.getAnimatedValue()).floatValue();
            this.titleView.setWidth((int) (this.measuredTitleWidth * value));
            if (value <= 0.0f) {
                this.titleView.setVisibility(8);
            }
        });
        animator.start();
        if (getBackground() instanceof TransitionDrawable) {
            TransitionDrawable trans = (TransitionDrawable) getBackground();
            trans.reverseTransition(this.animationDuration);
        } else if (!this.showShapeAlways) {
            setBackground(null);
        }
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setTitleTypeface(Typeface typeface) {
        this.titleView.setTypeface(typeface);
    }

    public void updateMeasurements(int maxWidth) {
        int marginLeft = 0;
        int marginRight = 0;
        ViewGroup.LayoutParams titleViewLayoutParams = this.titleView.getLayoutParams();
        if (titleViewLayoutParams instanceof RelativeLayout.LayoutParams) {
            marginLeft = ((RelativeLayout.LayoutParams) titleViewLayoutParams).rightMargin;
            marginRight = ((RelativeLayout.LayoutParams) titleViewLayoutParams).leftMargin;
        }
        int newTitleWidth = (((maxWidth - (getPaddingRight() + getPaddingLeft())) - (marginLeft + marginRight)) - ((int) this.bubbleToggleItem.getIconWidth())) + this.titleView.getPaddingRight() + this.titleView.getPaddingLeft();
        if (newTitleWidth > 0 && newTitleWidth < this.measuredTitleWidth) {
            this.measuredTitleWidth = this.titleView.getMeasuredWidth();
        }
    }

    public void setBadgeText(String value) {
        this.bubbleToggleItem.setBadgeText(value);
        updateBadge(getContext());
    }
}
