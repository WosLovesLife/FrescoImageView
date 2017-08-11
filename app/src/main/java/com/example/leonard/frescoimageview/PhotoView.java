package com.example.leonard.frescoimageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by leonard on 17/8/11.
 */

public class PhotoView extends SimpleDraweeView {
    public PhotoView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public PhotoView(Context context) {
        super(context);
    }

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PhotoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    float scale = 1f;

    @Override
    protected void onDraw(Canvas canvas) {

        Matrix matrix = new Matrix();

        matrix.preScale(getScale(), getScale());
        canvas.concat(matrix);

        super.onDraw(canvas);

    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        invalidate();
    }
}
