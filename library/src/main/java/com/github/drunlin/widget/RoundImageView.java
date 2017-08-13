package com.github.drunlin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

public class RoundImageView extends ShapedImageView {
    private final float[] radii = new float[8];

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.roundImageViewStyle);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray array = context.obtainStyledAttributes(
                attrs, R.styleable.RoundImageView, defStyleAttr, 0);

        final float cr = array.getDimension(R.styleable.RoundImageView_cornerRadius, 0);
        setCornerRadius(array.getDimension(R.styleable.RoundImageView_topLeftCornerRadius, cr),
                array.getDimension(R.styleable.RoundImageView_topRightCornerRadius, cr),
                array.getDimension(R.styleable.RoundImageView_bottomRightCornerRadius, cr),
                array.getDimension(R.styleable.RoundImageView_bottomLeftCornerRadius, cr));

        array.recycle();
    }

    public void setCornerRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        radii[0] = topLeft;
        radii[1] = topLeft;
        radii[2] = topRight;
        radii[3] = topRight;
        radii[4] = bottomRight;
        radii[5] = bottomRight;
        radii[6] = bottomLeft;
        radii[7] = bottomLeft;

        configureBounds();
        invalidate();
    }

    public float getTopLeftCornerRadius() {
        return radii[0];
    }

    public float getTopRightCornerRadius() {
        return radii[2];
    }

    public float getBottomRightCornerRadius() {
        return radii[4];
    }

    public float getBottomLeftCornerRadius() {
        return radii[6];
    }

    @Override
    protected void onBoundsChanged(RectF rect) {
        path.addRoundRect(rect, radii, Path.Direction.CW);
    }

    @Override
    public boolean isOpaque() {
        return super.isOpaque() && (isOpaqueInternal()
                || getTopLeftCornerRadius() <= 0 && getTopRightCornerRadius() <= 0
                && getBottomRightCornerRadius() <= 0 && getBottomLeftCornerRadius() <= 0);

    }
}
