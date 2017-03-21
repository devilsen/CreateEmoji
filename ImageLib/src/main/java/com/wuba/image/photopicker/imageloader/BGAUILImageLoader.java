package com.wuba.image.photopicker.imageloader;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/25 下午5:39
 * 描述:
 */
public class BGAUILImageLoader extends BGAImageLoader {

    private void initImageLoader(Context context) {
//        if (!ImageLoader.getInstance().isInited()) {
//            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
//            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context.getApplicationContext()).threadPoolSize(3).defaultDisplayImageOptions(options).build();
//            ImageLoader.getInstance().init(config);
//        }
    }

    @Override
    public void displayImage(Activity activity, ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height, final DisplayDelegate delegate) {
//        initImageLoader(activity);
//
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(loadingResId)
//                .showImageOnFail(failResId)
//                .cacheInMemory(true)
//                .build();
//        ImageSize imageSize = new ImageSize(width, height);
//
//        ImageLoader.getInstance().displayImage(getPath(path), new ImageViewAware(imageView), options, imageSize, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                if (delegate != null) {
//                    delegate.onSuccess(view, imageUri);
//                }
//            }
//        }, null);
    }

    @Override
    public void downloadImage(Context context, String path, final DownloadDelegate delegate) {
        initImageLoader(context);

//        ImageLoader.getInstance().loadImage(getPath(path), new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
//                if (delegate != null) {
//                    delegate.onSuccess(imageUri, loadedImage);
//                }
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                if (delegate != null) {
//                    delegate.onFailed(imageUri);
//                }
//            }
//        });
    }

    @Override
    public void pause(Activity activity) {
        initImageLoader(activity);
//        ImageLoader.getInstance().pause();
    }

    @Override
    public void resume(Activity activity) {
        initImageLoader(activity);
//        ImageLoader.getInstance().resume();
    }
}