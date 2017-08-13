package com.example.leonard.frescoimageview;

import android.content.res.Resources;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.ImmutableList;
import com.facebook.common.internal.Supplier;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.DrawableFactory;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.components.DeferredReleaser;
import com.facebook.drawee.gestures.GestureDetector;
import com.facebook.imagepipeline.animated.factory.AnimatedDrawableFactory;
import com.facebook.imagepipeline.cache.MemoryCache;
import com.facebook.imagepipeline.image.CloseableImage;

import java.util.concurrent.Executor;

/**
 * Created by zhangh on 2017/8/13.
 */

public class Controller extends PipelineDraweeController {
    public Controller(Resources resources, DeferredReleaser deferredReleaser, AnimatedDrawableFactory animatedDrawableFactory, Executor uiThreadExecutor, MemoryCache<CacheKey, CloseableImage> memoryCache, Supplier<DataSource<CloseableReference<CloseableImage>>> dataSourceSupplier, String id, CacheKey cacheKey, Object callerContext) {
        super(resources, deferredReleaser, animatedDrawableFactory, uiThreadExecutor, memoryCache, dataSourceSupplier, id, cacheKey, callerContext);
    }

    public Controller(Resources resources, DeferredReleaser deferredReleaser, AnimatedDrawableFactory animatedDrawableFactory, Executor uiThreadExecutor, MemoryCache<CacheKey, CloseableImage> memoryCache, Supplier<DataSource<CloseableReference<CloseableImage>>> dataSourceSupplier, String id, CacheKey cacheKey, Object callerContext, ImmutableList<DrawableFactory> drawableFactories) {
        super(resources, deferredReleaser, animatedDrawableFactory, uiThreadExecutor, memoryCache, dataSourceSupplier, id, cacheKey, callerContext, drawableFactories);
    }

    @Override
    protected void setGestureDetector(GestureDetector gestureDetector) {
        super.setGestureDetector(gestureDetector);
    }
}
