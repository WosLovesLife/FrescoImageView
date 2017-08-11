package com.example.leonard.frescoimageview;

import android.app.Application;
import android.graphics.Bitmap;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

/**
 * Created by leonard on 17/8/11.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this)
                .setMaxCacheSize(100 * 1024 * 1024)//最大缓存
                .setBaseDirectoryName("photo_cache")//子目录
                .setBaseDirectoryPathSupplier(new Supplier<File>() {
                    @Override
                    public File get() {
                        return getCacheDir();//还是推荐缓存到应用本身的缓存文件夹,这样卸载时能自动清除,其他清理软件也能扫描出来
                    }
                })
                .build();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setDownsampleEnabled(true)
                //Downsampling，要不要向下采样,它处理图片的速度比常规的裁剪scaling更快，
                // 并且同时支持PNG，JPG以及WEP格式的图片，非常强大,与ResizeOptions配合使用
                .setBitmapsConfig(Bitmap.Config.RGB_565)
//                .setBitmapMemoryCacheParamsSupplier(new MyBitmapMemoryCacheParamsSupplier((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)))
                .build();
        Fresco.initialize(this, config);
    }
}
