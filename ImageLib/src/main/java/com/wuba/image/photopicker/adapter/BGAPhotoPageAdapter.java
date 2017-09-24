package com.wuba.image.photopicker.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.wuba.image.photopicker.imageloader.BGAImage;
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
    private Context mContext;

    private int screenHeight;
    private int screenWidth;

    public BGAPhotoPageAdapter(Context context, PhotoViewAttacher.OnViewTapListener onViewTapListener, ArrayList<String> previewImages) {
        mOnViewTapListener = onViewTapListener;
        mPreviewImages = previewImages;
        mContext = context;

        screenHeight = BGAPhotoPickerUtil.getScreenHeight(context);
        screenWidth = BGAPhotoPickerUtil.getScreenWidth(context);
    }

    @Override
    public int getCount() {
        return mPreviewImages == null ? 0 : mPreviewImages.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {

        final BGAImageView imageView = new BGAImageView(container.getContext());
        container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final BGABrowserPhotoViewAttacher photoViewAttacher = new BGABrowserPhotoViewAttacher(imageView);
        photoViewAttacher.setOnViewTapListener(mOnViewTapListener);

        imageView.setDelegate(new BGAImageView.Delegate() {
            @Override
            public void onDrawableChanged(Drawable drawable) {
                if (drawable != null &&
                        drawable.getIntrinsicHeight() > drawable.getIntrinsicWidth() &&
                        drawable.getIntrinsicHeight() > screenHeight) {
                    photoViewAttacher.setIsSetTopCrop(true);
                    photoViewAttacher.setUpdateBaseMatrix();
                } else {
                    photoViewAttacher.update();
                }
            }
        });

        BGAImage.displayImage(mContext,
                imageView,
                mPreviewImages.get(position),
                screenWidth,
                screenHeight);

        return imageView;
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