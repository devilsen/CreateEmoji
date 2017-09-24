package com.wuba.image.photopicker.imageloader;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/9/8 下午1:05
 * 描述:
 */
public class BGARVOnScrollListener extends RecyclerView.OnScrollListener {
    private Context context;

    public BGARVOnScrollListener(Activity activity) {
        context = activity;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            BGAImage.resume(context);
        } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            BGAImage.pause(context);
        }
    }
}
