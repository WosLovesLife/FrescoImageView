package com.example.leonard.frescoimageview;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by rubenrenzema on 5/16/16.
 */
public class SpaceDecorator extends RecyclerView.ItemDecoration {

    private int space;

    public SpaceDecorator(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);

        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            setBorders(position, outRect, ((GridLayoutManager) parent.getLayoutManager()).getSpanCount(), true);
        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                setBorders(position, outRect, parent.getAdapter().getItemCount(), false);
            } else {
                setBorders(position, outRect, 1, true);
            }
        }
    }

    private void setBorders(int position, Rect outRect, int columns, boolean marginTopBottom) {
        int column = position % columns;
        int divider = columns > 3 ? 3 : columns;

        if (position < columns && marginTopBottom) {
            outRect.top = space;
        }
        if (column == 0) {
            outRect.right = space / divider * 2;

        } else if (column > 0) {
            if (column == (columns - 1)) {
                outRect.left = space / divider * 2;

            } else {
                outRect.left = space / divider;
                outRect.right = space / divider;
            }
        }
        if (marginTopBottom) {
            outRect.bottom = space;
        }
    }
}
