package com.example.leonard.frescoimageview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ProducerFactory;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.producers.NullProducer;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangh on 2017/8/12.
 */

public class BigPhotoActivity extends AppCompatActivity {

    private Adapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final BigPhotoView photoView = new BigPhotoView(this);
        photoView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER);
//        setContentView(photoView);

        Uri uri = getIntent().getParcelableExtra("photo_uri");
//        photoView.setImageURI(uri);

        photoView.setImageResource(R.drawable.long_image);

//        photoView.setScale(2.36f);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoView.setScale(photoView.getScale() + 0.5f);
            }
        });

//        LongImageView longImageView = new LongImageView(this);
//        try {
//            longImageView.setImageAssets(this, "sample2.jpg");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        FrameLayout frameLayout = new FrameLayout(this);
//        frameLayout.addView(longImageView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER));
//        setContentView(frameLayout);


        final RecyclerView recyclerView = new RecyclerView(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new Adapter();
        recyclerView.setAdapter(mAdapter);

        setContentView(recyclerView);

        final ScaleGestureDetector detector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return false;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }
        });

        loadImage(uri, new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
            @Override
            protected void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                if (!dataSource.isFinished()) {
                    return;
                }
                CloseableReference<CloseableImage> ref = dataSource.getResult();
                if (ref != null) {
                    try {
                        CloseableImage image = ref.get();
                        if (image instanceof CloseableBitmap) {
                            Bitmap bitmap = ((CloseableBitmap) image).getUnderlyingBitmap();

                            List<Bitmap> mBitmaps = new ArrayList<>();
                            int height = bitmap.getWidth();
                            long count = Math.round(bitmap.getHeight() * 1f / height + 0.5);
                            for (int i = 0; i < count; i++) {
                                int consumedHeight = height * i;
                                Bitmap bp = Bitmap.createBitmap(bitmap, 0, consumedHeight, bitmap.getWidth(), bitmap.getHeight() - consumedHeight > height ? height : bitmap.getHeight() - consumedHeight);
                                mBitmaps.add(bp);
                            }
                            mAdapter.mBitmapList = mBitmaps;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } finally {
                        CloseableReference.closeSafely(ref);
                    }
                }
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

            }
        });
    }

    private void loadImage(Uri uri, DataSubscriber<CloseableReference<CloseableImage>> subscriber) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).build();
        NullProducer<Object> producer = ProducerFactory.newNullProducer();
//        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, producer);
//        dataSource.subscribe(subscriber, new DefaultExecutorSupplier(1).forBackgroundTasks());

        imagePipeline.fetchEncodedImage(imageRequest, producer).subscribe(new DataSubscriber<CloseableReference<PooledByteBuffer>>() {
            @Override
            public void onNewResult(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                PooledByteBuffer pooledByteBuffer = dataSource.getResult().get();
                int size = pooledByteBuffer.size();
                byte[] bytes = new byte[size];
                pooledByteBuffer.read(0, bytes, 0, size);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, size);

                List<Bitmap> mBitmaps = new ArrayList<>();
                int height = bitmap.getWidth();
                long count = Math.round(bitmap.getHeight() * 1f / height + 0.5);
                for (int i = 0; i < count; i++) {
                    int consumedHeight = height * i;
                    Bitmap bp = Bitmap.createBitmap(bitmap, 0, consumedHeight, bitmap.getWidth(), bitmap.getHeight() - consumedHeight > height ? height : bitmap.getHeight() - consumedHeight);
                    mBitmaps.add(bp);
                }
                mAdapter.mBitmapList = mBitmaps;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
                Log.w("Leonard", "onNewResult: " + size);
            }

            @Override
            public void onFailure(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {

            }

            @Override
            public void onCancellation(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {

            }

            @Override
            public void onProgressUpdate(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {

            }
        }, new DefaultExecutorSupplier(1).forBackgroundTasks());
    }

    class Adapter extends RecyclerView.Adapter<Holder> {
        List<Bitmap> mBitmapList;

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_image_part, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.mImagePart.setImageBitmap(mBitmapList.get(position));
        }

        @Override
        public int getItemCount() {
            return mBitmapList != null ? mBitmapList.size() : 0;
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        ImagePart mImagePart;

        public Holder(View itemView) {
            super(itemView);
            mImagePart = (ImagePart) itemView;
        }
    }
}
