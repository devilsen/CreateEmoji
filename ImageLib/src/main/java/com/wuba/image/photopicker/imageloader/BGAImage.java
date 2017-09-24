package com.wuba.image.photopicker.imageloader;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.wuba.image.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/25 下午5:03
 * 描述:
 */
public class BGAImage {

    private static BGAImageLoader sImageLoader;

    private BGAImage() {
    }

    private static BGAImageLoader getImageLoader() {
        if (sImageLoader == null)
            sImageLoader = new BGAGlideImageLoader();
        return sImageLoader;
    }

    public static void displayImage(Context context,
                                    ImageView imageView,
                                    String path,
                                    int width,
                                    int height) {

        displayImage(context, imageView, path, R.mipmap.bga_pp_ic_holder_dark, R.mipmap.bg_pic_black_break, width, height);
    }

    public static void displayImage(Context context,
                                    ImageView imageView,
                                    String path,
                                    @DrawableRes int loadingResId,
                                    @DrawableRes int failResId,
                                    int width,
                                    int height) {

        displayImage(context, imageView, path, loadingResId, failResId, width, height, null);
    }

    public static void displayImage(Context context,
                                    ImageView imageView,
                                    String path,
                                    @DrawableRes int loadingResId,
                                    @DrawableRes int failResId,
                                    int width,
                                    int height,
                                    final BGAImageLoader.DisplayDelegate delegate) {

        getImageLoader().displayImage(context, imageView, path, loadingResId, failResId, width, height, delegate);
    }

    public static void downloadImage(Context context, String path, final BGAImageLoader.DownloadDelegate delegate) {
        getImageLoader().downloadImage(context, path, delegate);
    }


    public static void pause(Context context) {
        getImageLoader().pause(context);
    }

    public static void resume(Context context) {
        getImageLoader().resume(context);
    }

}
