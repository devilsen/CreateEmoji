package com.wuba.image.photopicker.imageloader;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/25 下午6:03
 * 描述:
 */
public class BGAXUtilsImageLoader extends BGAImageLoader {

    @Override
    public void displayImage(Context context, final ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height, final DisplayDelegate delegate) {
//        x.Ext.init(activity.getApplication());
//
//        ImageOptions options = new ImageOptions.Builder()
//                .setLoadingDrawableId(loadingResId)
//                .setFailureDrawableId(failResId)
//                .setSize(width, height)
//                .build();
//
//        final String finalPath = getPath(path);
//        x.image().bind(imageView, finalPath, options, new Callback.CommonCallback<Drawable>() {
//            @Override
//            public void onSuccess(Drawable result) {
//                if (delegate != null) {
//                    delegate.onSuccess(imageView, finalPath);
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//            }
//
//            @Override
//            public void onFinished() {
//            }
//        });
    }

    @Override
    public void downloadImage(Context context, String path, final DownloadDelegate delegate) {
//        x.Ext.init((Application) context.getApplicationContext());
//
//        final String finalPath = getPath(path);
//        x.image().loadDrawable(finalPath, new ImageOptions.Builder().build(), new Callback.CommonCallback<Drawable>() {
//            @Override
//            public void onSuccess(Drawable result) {
//                if (delegate != null) {
//                    delegate.onSuccess(finalPath, ((BitmapDrawable) result).getBitmap());
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                if (delegate != null) {
//                    delegate.onFailed(finalPath);
//                }
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//            }
//
//            @Override
//            public void onFinished() {
//            }
//        });
    }

    @Override
    public void pause(Context context) {
    }

    @Override
    public void resume(Context context) {

    }
}