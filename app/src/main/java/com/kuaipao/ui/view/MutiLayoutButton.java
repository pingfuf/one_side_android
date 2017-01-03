package com.kuaipao.ui.view;

import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class MutiLayoutButton extends View {

    private static final long ANIMATION_DURATION = 200;
    private static final int StateNormal = 1;
    private static final int StateTouchDown = 2;
    private static final int StateTouchUp = 3;
    private static final float SHADOW_RADIUS = 8.0f;
    private static final float SHADOW_OFFSET_X = 0.0f;
    private static final float SHADOW_OFFSET_Y = 4.0f;
    private static final float MIN_SHADOW_COLOR_ALPHA = 0.1f;
    private static final float MAX_SHADOW_COLOR_ALPHA = 0.4f;

    private int mState = StateNormal;
    private long mStartTime;
    private int mColor;
    private int mShadowColor;
    private int mCornerRadius;
    private int mPadding;
    private int mTextSize;
    private int defaultTextColor;
    private int[] mTextColors;
    private float mShadowRadius;
    private float mShadowOffsetX;
    private float mShadowOffsetY;
    private CharSequence[] mTexts;
    private RectF backgroundRectF;
    private Rect mFingerRect;
    private Path rippleClipPath;
    private boolean mMoveOutside;
    private Point mTouchPoint = new Point();

    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ripplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint[] mTextPaints;
    private int NumOfButton = 1;
    private StaticLayout layout;
    private boolean useSpan = false;
    private int which = 0;


    public MutiLayoutButton(Context context) {
        this(context, null);
    }

    public MutiLayoutButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    @SuppressLint("NewApi")// api >= 11
    public MutiLayoutButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPadding = 0;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PaperButton);
        mColor = attributes.getColor(R.styleable.PaperButton_paper_color,
                getResources().getColor(R.color.paper_button_color));
        mShadowColor = attributes.getColor(R.styleable.PaperButton_paper_shadow_color,
                getResources().getColor(R.color.paper_button_shadow_color));
        mCornerRadius = attributes.getDimensionPixelSize(R.styleable.PaperButton_paper_corner_radius,
                getResources().getDimensionPixelSize(R.dimen.paper_button_corner_radius));
        NumOfButton = attributes.getInteger(R.styleable.PaperButton_paper_number_of_button, 1);
        setNumOfButton(NumOfButton);
        mTextSize = getResources().getDimensionPixelSize(R.dimen.paper_text_size);
        if (mTextColors.length > 1) {
            for (int i = 1; i < NumOfButton; i++) {
                mTextColors[i] = getResources().getColor(R.color.text_color_deep_gray);
            }
        }
        defaultTextColor = getResources().getColor(R.color.text_color_deep_gray);
        final String assetPath = attributes.getString(R.styleable.PaperButton_paper_font);
        if (assetPath != null) {
            AssetManager assets = context.getAssets();
            Typeface typeface = Typeface.createFromAsset(assets, assetPath);
            for (int i = 0; i < NumOfButton; i++) {
                mTextPaints[i].setTypeface(typeface);
            }
        }
        mShadowRadius = attributes.getFloat(R.styleable.PaperButton_paper_shadow_radius, SHADOW_RADIUS);
        mShadowOffsetX = attributes.getFloat(R.styleable.PaperButton_paper_shadow_offset_x, SHADOW_OFFSET_X);
        mShadowOffsetY = attributes.getFloat(R.styleable.PaperButton_paper_shadow_offset_y, SHADOW_OFFSET_Y);
        attributes.recycle();

        backgroundPaint.setColor(mColor);
        backgroundPaint.setStyle(Paint.Style.FILL);
        int shadowColor = changeColorAlpha(mShadowColor, MIN_SHADOW_COLOR_ALPHA);
        backgroundPaint.setShadowLayer(mShadowRadius, mShadowOffsetX, mShadowOffsetY, shadowColor);

        for (int i = 0; i < NumOfButton; i++) {
            mTextPaints[i].setColor(mTextColors[i]);
            mTextPaints[i].setTextSize(mTextSize);
            mTextPaints[i].setTextAlign(TextPaint.Align.CENTER);
        }

        ripplePaint.setColor(darkenColor(mColor));

        setWillNotDraw(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);// api >= 11
        }
    }

    private int changeColorAlpha(int color, float value) {
        int alpha = Math.round(Color.alpha(color) * value);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.9f;
        return Color.HSVToColor(hsv);
    }

    public void setNumOfButton(int num) {
        NumOfButton = num;
        mTexts = new CharSequence[num];
        mTextPaints = new TextPaint[num];
        for (int i = 0; i < num; i++) {
            mTextPaints[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        }
        mTextColors = new int[num];
    }

    public void setColor(int color) {
        mColor = color;
        backgroundPaint.setColor(mColor);
        invalidate();
    }

    public void setShadowColor(int color) {
        mShadowColor = color;
        backgroundPaint.setShadowLayer(mShadowRadius, mShadowOffsetX, mShadowOffsetY, mShadowColor);
        invalidate();
    }

    public void setTextSize(int pixel) {
        mTextSize = pixel;
        for (int i = 0; i < NumOfButton; i++) {
            mTextPaints[i].setTextSize(mTextSize);
        }
        invalidate();
    }

    public void setTextColor(int color) {
        for (int i = 0; i < NumOfButton; i++) {
            mTextColors[i] = color;
            mTextPaints[i].setColor(mTextColors[i]);
        }
        invalidate();
    }

    public void setTextColorWithIndex(int color, int index) {
        for (int i = 0; i < NumOfButton; i++) {
            mTextColors[i] = defaultTextColor;
            mTextPaints[i].setColor(mTextColors[i]);
        }
        mTextColors[index] = color;
        mTextPaints[index].setColor(color);
        invalidate();
    }

    public void setText(String[] texts) {
        int length = texts.length;
        for (int i = 0; i < length; i++) {
            mTexts[i] = texts[i];
        }
        useSpan = false;
        invalidate();
    }

    public void setSpanString(Spannable[] spannables) {
        int length = spannables.length;
        for (int i = 0; i < length; i++) {
            mTexts[i] = spannables[i];
        }
        useSpan = true;
        invalidate();
    }


    public int whichIsClick() {
        return which;
    }

    private RectF getRectF() {
        if (backgroundRectF == null) {
            backgroundRectF = new RectF();
            backgroundRectF.left = mPadding;
            backgroundRectF.top = mPadding;
            backgroundRectF.right = getWidth() - mPadding;
            backgroundRectF.bottom = getHeight() - mPadding;
        }
        return backgroundRectF;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMoveOutside = false;
                mFingerRect = new Rect(getLeft(), getTop(), getRight(), getBottom());
                which = (int) (event.getX() * NumOfButton / getWidth());
                mTouchPoint.set(Math.round(((which * 2 + 1) * getWidth()) / (NumOfButton * 2)
                ), Math.round(getHeight() / 2));
                mState = StateTouchDown;
                mStartTime = System.currentTimeMillis();
                invalidate();

                break;
            case MotionEvent.ACTION_MOVE:
                if (!mFingerRect.contains(getLeft() + (int) event.getX(),
                        getTop() + (int) event.getY())) {
                    mMoveOutside = true;
                    mState = StateNormal;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!mMoveOutside) {
                    mState = StateTouchUp;
                    mStartTime = System.currentTimeMillis();
                    invalidate();
                    performClick();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mState = StateNormal;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = 0;
        int shadowColor = changeColorAlpha(mShadowColor, MIN_SHADOW_COLOR_ALPHA);
        long elapsed = System.currentTimeMillis() - mStartTime;
        switch (mState) {
            case StateNormal:
                shadowColor = changeColorAlpha(mShadowColor, MIN_SHADOW_COLOR_ALPHA);
                break;
            case StateTouchDown:
                ripplePaint.setAlpha(255);
                if (elapsed < ANIMATION_DURATION) {
                    radius = Math.round(elapsed * getWidth() / (NumOfButton * 2) / ANIMATION_DURATION);
                    float shadowAlpha = (MAX_SHADOW_COLOR_ALPHA - MIN_SHADOW_COLOR_ALPHA)
                            * elapsed
                            / ANIMATION_DURATION
                            + MIN_SHADOW_COLOR_ALPHA;
                    shadowColor = changeColorAlpha(mShadowColor, shadowAlpha);
                } else {
                    radius = getWidth() / (NumOfButton * 2);
                    shadowColor = changeColorAlpha(mShadowColor, MAX_SHADOW_COLOR_ALPHA);
                }
                postInvalidate();
                break;
            case StateTouchUp:
                if (elapsed < ANIMATION_DURATION) {
                    int alpha = Math.round((ANIMATION_DURATION - elapsed) * 255 / ANIMATION_DURATION);
                    ripplePaint.setAlpha(alpha);
                    radius = getWidth() / (NumOfButton * 2) + Math.round(elapsed * getWidth() / (NumOfButton * 2) / ANIMATION_DURATION);
                    float shadowAlpha = (MAX_SHADOW_COLOR_ALPHA - MIN_SHADOW_COLOR_ALPHA)
                            * (ANIMATION_DURATION - elapsed)
                            / ANIMATION_DURATION
                            + MIN_SHADOW_COLOR_ALPHA;
                    shadowColor = changeColorAlpha(mShadowColor, shadowAlpha);
                } else {
                    mState = StateNormal;
                    radius = 0;
                    ripplePaint.setAlpha(0);
                    shadowColor = changeColorAlpha(mShadowColor, MIN_SHADOW_COLOR_ALPHA);
                }
                postInvalidate();
                break;
        }
        backgroundPaint.setShadowLayer(mShadowRadius, mShadowOffsetX, mShadowOffsetY, shadowColor);
        canvas.drawRoundRect(getRectF(), mCornerRadius, mCornerRadius, backgroundPaint);
        canvas.save();
        if (mState == StateTouchDown || mState == StateTouchUp) {
            if (rippleClipPath == null) {
                rippleClipPath = new Path();
                rippleClipPath.addRoundRect(getRectF(), mCornerRadius, mCornerRadius, Path.Direction.CW);
            }
            canvas.clipPath(rippleClipPath);
        }
        canvas.drawCircle(mTouchPoint.x, mTouchPoint.y, radius, ripplePaint);
        canvas.restore();
        if (useSpan) {
            CharSequence mText0 = mTexts[0];
            TextPaint textPaint0 = mTextPaints[0];
            textPaint0.setColor(mTextColors[0]);
            if (mText0 != null && mText0.length() > 0) {
                int y = (int) (getHeight() / 2 - ((textPaint0.descent() + textPaint0.ascent()) / 2));
                int x = (int) ((getWidth() / NumOfButton - textPaint0.measureText(mText0.toString())) / 2);

                layout = new StaticLayout(mText0, textPaint0, getWidth(), Alignment.ALIGN_NORMAL, 1, 0, false);
                canvas.translate(x, y / 2);
                layout.draw(canvas);
            }
            for (int i = 1; i < NumOfButton; i++) {
                CharSequence mText = mTexts[i];
                TextPaint textPaint = mTextPaints[i];
                textPaint.setColor(mTextColors[i]);
                if (mText != null && mText.length() > 0) {
                    int y = (int) (getHeight() / 2 - ((textPaint.descent() + textPaint.ascent()) / 2));
                    int x = (int) (getWidth() / NumOfButton);
                    layout = new StaticLayout(mText, textPaint, getWidth(), Alignment.ALIGN_NORMAL, 1, 0, false);
                    canvas.translate(x, 0);
                    layout.draw(canvas);
                }
            }
        } else {
            for (int i = 0; i < NumOfButton; i++) {
                CharSequence mText = mTexts[i];
                TextPaint textPaint = mTextPaints[i];
                textPaint.setTextSize(ViewUtils.rp(14));
                textPaint.setColor(mTextColors[i]);
                if (mText != null && mText.length() > 0) {
                    int y = (int) (getHeight() / 2 - ((textPaint.descent() + textPaint.ascent()) / 2));
                    int x = (int) (getWidth() / NumOfButton * i + (getWidth() / NumOfButton - textPaint.measureText(mText.toString())) / 2);
                    canvas.drawText(mText, 0, mText.length(), x, y, textPaint);
                }
            }
        }
    }
}

