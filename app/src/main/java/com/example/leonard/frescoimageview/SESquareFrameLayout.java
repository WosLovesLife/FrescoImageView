package com.example.leonard.frescoimageview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class SESquareFrameLayout extends FrameLayout {

    public SESquareFrameLayout(Context context) {
        super(context);
    }

    public SESquareFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SESquareFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}