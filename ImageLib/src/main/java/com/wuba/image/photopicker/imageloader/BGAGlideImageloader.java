package com.wuba.image.photopicker.imageloader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableTransformation;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/25 下午4:40
 * 描述:
 */
class BGAGlideImageLoader extends BGAImageLoader {

    @Override
    public void displayImage(Context context, final ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height, final DisplayDelegate delegate) {
        final String finalPath = getPath(path);
//        Glide.with(activity)
//                .load(finalPath)
//                .thumbnail(0.3f)
//                .placeholder(loadingResId)
//                .error(failResId)
//                .override(width, height)
//                .into(imageView);


        Glide.with(context)
                .load(finalPath)
                .thumbnail(0.3f)
                .placeholder(loadingResId)
                .error(failResId)
                .dontAnimate()
                .override(width, height)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
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
    public void downloadImage(Context context, @NonNull String path, final DownloadDelegate delegate) {
        final String finalPath = getPath(path);
        if (!path.endsWith(".gif")) {
            Glide.with(context.getApplicationContext())
                    .load(finalPath)
                    .asBitmap()
                    .toBytes()
                    .into(new SimpleTarget<byte[]>() {
                        @Override
                        public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                            if (delegate != null) {
                                delegate.onSuccess(finalPath, resource);
                            }
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            if (delegate != null) {
                                delegate.onFailed(finalPath);
                            }
                        }
                    });
        } else {
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .asGif()
                    .toBytes()
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) // resize to this size
                    // transformation to use for resize
                    .transform(new GifDrawableTransformation(new FitCenter(context), Glide.get(context).getBitmapPool()))
                    .into(new SimpleTarget<byte[]>() {
                        @Override
                        public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                            if (delegate != null) {
                                delegate.onSuccess(finalPath, resource);
                            }
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            if (delegate != null) {
                                delegate.onFailed(finalPath);
                            }
                        }
                    });
        }
    }

    @Override
    public void pause(Context context) {
        Glide.with(context.getApplicationContext()).pauseRequests();
    }

    @Override
    public void resume(Context context) {
        Glide.with(context.getApplicationContext()).resumeRequestsRecursive();
    }
}