package com.example.leonard.frescoimageview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ImagePickerImageViewHolder extends RecyclerView.ViewHolder {
    public PhotoView mDraweeView;

    public ImagePickerImageViewHolder(View view) {
        super(view);
        mDraweeView = (PhotoView) view.findViewById(R.id.image);
    }
}
