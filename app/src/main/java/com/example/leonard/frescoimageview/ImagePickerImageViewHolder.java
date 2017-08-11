package com.example.leonard.frescoimageview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

public class ImagePickerImageViewHolder extends RecyclerView.ViewHolder {
    public SimpleDraweeView mDraweeView;

    public ImagePickerImageViewHolder(View view) {
        super(view);
        mDraweeView = (SimpleDraweeView) view.findViewById(R.id.image);
    }
}
