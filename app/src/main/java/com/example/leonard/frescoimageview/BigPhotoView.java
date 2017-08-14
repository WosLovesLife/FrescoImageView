package com.example.leonard.frescoimageview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ProducerFactory;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.producers.NullProducer;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by zhangh on 2017/8/12.
 */

public class BigPhotoView extends SimpleDraweeView {

    private Paint mPaint;
    private Bitmap mBitmap;

    public BigPhotoView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init();
    }

    public BigPhotoView(Context context) {
        super(context);
        init();
    }

    public BigPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BigPhotoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BigPhotoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    float scale = 1f;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Matrix matrix = canvas.getMatrix();
//        matrix.preScale(getScale(), getScale(), getWidth() / 2, 0);
//        canvas.concat(matrix);
//        super.onDraw(canvas);


//        Matrix matrix = canvas.getMatrix();
//        matrix.preScale(getScale(), getScale(), getWidth() / 2, 0);
//        canvas.concat(matrix);

        if (mBitmap != null) {
            int height = getHeight();
            Bitmap bitmap2 = Bitmap.createBitmap(mBitmap, 0, mCurrentOffsetY, mBitmap.getWidth(), mBitmap.getHeight() - mCurrentOffsetY > height ? height : mBitmap.getHeight() - mCurrentOffsetY);
            canvas.drawBitmap(bitmap2, 0, 0, mPaint);
        }
    }

    public void setPhoto(Uri uri) {
        loadImage(uri);
    }

    private void loadImage(Uri uri) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).build();
        NullProducer<Object> producer = ProducerFactory.newNullProducer();
//        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, producer);
//        dataSource.subscribe(subscriber, new DefaultExecutorSupplier(1).forBackgroundTasks());

        imagePipeline.fetchEncodedImage(imageRequest, producer).subscribe(new DataSubscriber<CloseableReference<PooledByteBuffer>>() {
            @Override
            public void onNewResult(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                if (!dataSource.isFinished()) {
                    return;
                }
                PooledByteBuffer pooledByteBuffer = dataSource.getResult().get();
                int size = pooledByteBuffer.size();
                Log.w("Leonard", "onNewResult: " + size);
                byte[] bytes = new byte[size];
                pooledByteBuffer.read(0, bytes, 0, size);
                mBitmap = BitmapFactory.decodeByteArray(bytes, 0, size);

//                List<Bitmap> mBitmaps = new ArrayList<>();
//                int height = mBitmap.getWidth();
//                long count = Math.round(mBitmap.getHeight() * 1f / height + 0.5);
//                for (int i = 0; i < count; i++) {
//                    int consumedHeight = height * i;
//                    Bitmap bp = Bitmap.createBitmap(mBitmap, 0, consumedHeight, mBitmap.getWidth(), mBitmap.getHeight() - consumedHeight > height ? height : mBitmap.getHeight() - consumedHeight);
//                    mBitmaps.add(bp);
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mAdapter.notifyDataSetChanged();
//                    }
//                });

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
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


    public float getScale() {
        float width = getDrawable().getBounds().width();
        return getWidth() / width;
    }

    public void setScale(float scale) {
        this.scale = scale;
        invalidate();
    }

    private ScrollerCompat mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;
    private int mScrollPointerId;
    private int mIgnoreEdge;
    private ValueAnimator mValueAnimator;

    float mDownX;
    float mDownY;
    float mLastX;
    float mLastY;

    private int mCurrentOffsetY;
    private int mMinOffsetY;
    private int mMaxOffsetY;

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mScroller = ScrollerCompat.create(getContext(), new DecelerateInterpolator());
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();

        mMinOffsetY = 0;
        mMaxOffsetY = 10000;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean consume = true;
        boolean addVelocityTracker = false;
        float x = ev.getRawX();
        float y = ev.getRawY();

        MotionEvent vtev = MotionEvent.obtain(ev);

        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                mDownY = y;
                mScrollPointerId = ev.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = mLastX - x;
                float deltaY = mLastY - y;
                if (Math.abs(mDownX - x) > mTouchSlop && Math.abs(deltaX) > Math.abs(deltaY)) {
                    consume = false;
                    break;
                }
                if ((mCurrentOffsetY < mMaxOffsetY && deltaY < 0) || (mCurrentOffsetY > mMinOffsetY && deltaY > 0)) {
                }
                setOffsetBy(deltaY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                addVelocityTracker = true;
                initVelocityTrackerIfNotExists();
                mVelocityTracker.addMovement(vtev);
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                float yVelocity = VelocityTrackerCompat.getYVelocity(mVelocityTracker, mScrollPointerId);
                fling(-yVelocity);
                break;
        }

        if (!addVelocityTracker) {
            initVelocityTrackerIfNotExists();
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();

        mLastX = x;
        mLastY = y;
        return consume;
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void fling(float yVelocity) {
        mScroller.fling(0, mCurrentOffsetY, 0, (int) -yVelocity, 0, 0, 0, 10000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            setOffsetTo(mScroller.getCurrY());

        }
    }

    private void setOffsetBy(float y) {
        float height = mCurrentOffsetY - y;
        setOffsetTo(height);
    }

    private void setOffsetTo(float y) {
        if (y == mCurrentOffsetY) return;
        if (y < mMinOffsetY) y = mMinOffsetY;
        if (y > mMaxOffsetY) y = mMaxOffsetY;
        if (y == mCurrentOffsetY) return;

        mCurrentOffsetY = (int) y;

        Log.w("Leonard", "setOffsetTo: " + mCurrentOffsetY);

        if (mCurrentOffsetY <= mMinOffsetY) {
//            mOffsetLocked = true;
        }

        setScrollY(mCurrentOffsetY);

        invalidate();
    }
}
