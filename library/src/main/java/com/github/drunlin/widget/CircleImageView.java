package com.github.drunlin.widget;

import android.content.Context;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

public class CircleImageView extends ShapedImageView {
    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onBoundsChanged(RectF rect) {
        path.addOval(rect, Path.Direction.CW);
    }

    @Override
    public boolean isOpaque() {
        return super.isOpaque() && isOpaqueInternal();
    }
}
