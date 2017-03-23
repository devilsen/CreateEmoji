package com.wuba.image.photopicker.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/25 下午4:40
 * 描述:
 */
public class BGAGlideImageloader extends BGAImageLoader {

    @Override
    public void displayImage(Activity activity, final ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height, final DisplayDelegate delegate) {
        final String finalPath = getPath(path);
//        Glide.with(activity)
//                .load(finalPath)
//                .thumbnail(0.3f)
//                .placeholder(loadingResId)
//                .error(failResId)
//                .override(width, height)
//                .into(imageView);


        Glide.with(activity)
                .load(finalPath)
                .thumbnail(0.3f)
                .placeholder(loadingResId)
                .error(failResId)
                .override(width, height)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if (delegate != null) {
                            delegate.onFail();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (delegate != null) {
                            delegate.onSuccess(imageView, finalPath);
                        }
                        return false;
                    }
                }).into(imageView);

    }

    @Override
    public void downloadImage(Context context, String path, final DownloadDelegate delegate) {
        final String finalPath = getPath(path);
        Glide.with(context.getApplicationContext()).load(finalPath).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (delegate != null) {
                    delegate.onSuccess(finalPath, resource);
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                if (delegate != null) {
                    delegate.onFailed(finalPath);
                }
            }
        });
    }

    @Override
    public void pause(Activity activity) {
        Glide.with(activity.getApplicationContext()).pauseRequests();
    }

    @Override
    public void resume(Activity activity) {
        Glide.with(activity.getApplicationContext()).resumeRequestsRecursive();
    }
}