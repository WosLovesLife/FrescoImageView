package com.example.leonard.frescoimageview;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;

public class ImagePickerRecyclerviewAdapter extends RecyclerView.Adapter<ImagePickerImageViewHolder> {

    private static final int TYPE_ACTION_CAMERA = 0;
    private static final int TYPE_ACTION_SEARCH = 1;
    private static final int TYPE_IMAGE = 2;

    AppCompatActivity mActivity;
    private ArrayList<String> itemList;

    public ImagePickerRecyclerviewAdapter(AppCompatActivity activity, ArrayList<String> itemList) {
        this.mActivity = activity;
        this.itemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_ACTION_CAMERA : position == 1 ? TYPE_ACTION_SEARCH : TYPE_IMAGE;
    }

    @Override
    public ImagePickerImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_image_picker, parent, false);
        return new ImagePickerImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImagePickerImageViewHolder holder, int position) {
        String url = itemList.get(position);
        if (url != null && url.startsWith("/")) {
            url = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_FILE_SCHEME)
                    .path(url)
                    .build().toString();
        }

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).setResizeOptions(new ResizeOptions(200, 200)).build();
        AbstractDraweeController build = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setAutoPlayAnimations(true)
                .setOldController(holder.mDraweeView.getController())
                .build();
        holder.mDraweeView.setController(build);

        holder.mDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 17/8/11
                holder.mDraweeView.setScale(1.5f);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public void add(String url) {
        itemList.add(url);
        notifyItemInserted(itemList.size() - 1);
    }


    public boolean containsItemList(String item) {
        return itemList.contains(item);
    }

    public void addListHead(String url, int index) {
        itemList.add(index, url);
        notifyItemInserted(index);
    }

    public void clear() {
        itemList.clear();
        notifyDataSetChanged();
    }

    public int getPosition(String url) {
        return itemList.indexOf(url);
    }
}