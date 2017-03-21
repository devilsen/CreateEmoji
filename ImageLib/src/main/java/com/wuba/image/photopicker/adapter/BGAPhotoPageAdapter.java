package com.wuba.image.photopicker.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.wuba.image.R;
import com.wuba.image.photopicker.imageloader.BGAImage;
import com.wuba.image.photopicker.imageloader.BGAImageLoader;
import com.wuba.image.photopicker.util.BGABrowserPhotoViewAttacher;
import com.wuba.image.photopicker.util.BGAPhotoPickerUtil;
import com.wuba.image.photopicker.widget.BGAImageView;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/27 下午6:35
 * 描述:
 */
public class BGAPhotoPageAdapter extends PagerAdapter {
    private ArrayList<String> mPreviewImages;
    private PhotoViewAttacher.OnViewTapListener mOnViewTapListener;
    private Activity mActivity;

    public BGAPhotoPageAdapter(Activity activity, PhotoViewAttacher.OnViewTapListener onViewTapListener, ArrayList<String> previewImages) {
        mOnViewTapListener = onViewTapListener;
        mPreviewImages = previewImages;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return mPreviewImages == null ? 0 : mPreviewImages.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        FrameLayout rootView = (FrameLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.bga_pp_item_photo, container, false);

        final BGAImageView imageView = (BGAImageView) rootView.findViewById(R.id.hvp_photo_preview_img);
        final ProgressWheel progress = (ProgressWheel) rootView.findViewById(R.id.hvp_photo_preview_progress);

        container.addView(rootView);
        final BGABrowserPhotoViewAttacher photoViewAttacher = new BGABrowserPhotoViewAttacher(imageView);
        photoViewAttacher.setOnViewTapListener(mOnViewTapListener);
        imageView.setDelegate(new BGAImageView.Delegate() {
            @Override
            public void onDrawableChanged(Drawable drawable) {
                if (drawable != null && drawable.getIntrinsicHeight() > drawable.getIntrinsicWidth() && drawable.getIntrinsicHeight() > BGAPhotoPickerUtil.getScreenHeight(imageView.getContext())) {
                    photoViewAttacher.setIsSetTopCrop(true);
                    photoViewAttacher.setUpdateBaseMatrix();
                } else {
                    photoViewAttacher.update();
                }
            }
        });

        BGAImage.displayImage(mActivity, imageView,
                mPreviewImages.get(position), R.mipmap.bga_pp_ic_holder_dark, R.mipmap.bg_pic_black_break,
                BGAPhotoPickerUtil.getScreenWidth(imageView.getContext()), BGAPhotoPickerUtil.getScreenHeight(imageView.getContext()),
                new BGAImageLoader.DisplayDelegate() {
                    @Override
                    public void onSuccess(View view, String path) {
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(mActivity, "加载失败", Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                    }
                });

        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public String getItem(int position) {
        return mPreviewImages == null ? "" : mPreviewImages.get(position);
    }
}