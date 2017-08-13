package com.example.leonard.frescoimageview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by zhangh on 2017/8/13.
 */

public class ImagePart extends ImageView {
    public ImagePart(Context context) {
        super(context);
    }

    public ImagePart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImagePart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Drawable drawable = getDrawable();
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            float ratio = getMeasuredWidth() * 1f / intrinsicWidth;
            int height = (int) (ratio * getMeasuredHeight());
            setMeasuredDimension(getMeasuredWidth(), height);
        }
    }
}
