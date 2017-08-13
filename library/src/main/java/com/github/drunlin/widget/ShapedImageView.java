package com.github.drunlin.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class ShapedImageView extends AppCompatImageView {
    protected final Path path = new Path();

    private final Paint paint;

    public ShapedImageView(Context context) {
        this(context, null);
    }

    public ShapedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        configureBounds();
    }

    @Override
    public void setPadding(@Px int left, @Px int top, @Px int right, @Px int bottom) {
        super.setPadding(left, top, right, bottom);

        configureBounds();
    }

    @Override
    public void setPaddingRelative(@Px int start, @Px int top, @Px int end, @Px int bottom) {
        super.setPaddingRelative(start, top, end, bottom);

        configureBounds();
    }

    protected void configureBounds() {
        final int width = getWidth();
        final int height = getHeight();

        if (width <= 0 || height <= 0) return;

        final RectF rect = new RectF(getPaddingLeft(), getPaddingTop(),
                width - getPaddingRight(), height - getPaddingBottom());
        onBoundsChanged(rect);
    }

    protected void onBoundsChanged(RectF rect) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int saveCount = canvas.getSaveCount();

        if (isHardwareAccelerated()) {
            if (getBackground() != null) {
                canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
            }

            super.onDraw(canvas);

            canvas.drawPath(path, paint);
        } else {
            canvas.saveLayerAlpha(0, 0, getWidth(), getHeight(), 0xFF, Canvas.ALL_SAVE_FLAG);

            super.onDraw(canvas);

            canvas.drawBitmap(createMaskBitmap(), 0, 0, paint);
        }

        canvas.restoreToCount(saveCount);
    }

    @NonNull
    private Bitmap createMaskBitmap() {
        final Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawPath(path, new Paint(Paint.ANTI_ALIAS_FLAG));
        return bitmap;
    }

    protected boolean isOpaqueInternal() {
        return getBackground() != null && getBackground().getOpacity() == PixelFormat.OPAQUE
                || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && getForeground() != null && getForeground().getOpacity() == PixelFormat.OPAQUE;

    }
}
