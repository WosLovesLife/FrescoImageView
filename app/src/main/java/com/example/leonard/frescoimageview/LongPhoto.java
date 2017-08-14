package com.example.leonard.frescoimageview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by zhangh on 2017/8/14.
 */

public class LongPhoto extends SimpleDraweeView {
    public LongPhoto(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public LongPhoto(Context context) {
        super(context);
    }

    public LongPhoto(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LongPhoto(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LongPhoto(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
