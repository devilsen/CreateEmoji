package com.wuba.image.photopicker.imageloader;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/25 下午4:30
 * 描述:
 */
public abstract class BGAImageLoader {

    protected String getPath(String path) {
        if (path == null) {
            path = "";
        }

        if (!path.startsWith("http") && !path.startsWith("file")) {
            path = "file://" + path;
        }
        return path;
    }

    public abstract void displayImage(Context context, ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height, DisplayDelegate delegate);

    public abstract void downloadImage(Context context, String path, DownloadDelegate delegate);

    public abstract void pause(Context context);

    public abstract void resume(Context context);

    public interface DisplayDelegate {
        void onSuccess(View view, String path);

        void onFail();
    }

    public interface DownloadDelegate {
        void onSuccess(String path, byte[] resource);

        void onFailed(String path);
    }
}