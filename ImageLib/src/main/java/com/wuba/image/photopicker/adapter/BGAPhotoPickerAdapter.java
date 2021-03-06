package com.wuba.image.photopicker.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.wuba.image.R;
import com.wuba.image.photopicker.imageloader.BGAImage;
import com.wuba.image.photopicker.model.BGAImageFolderModel;
import com.wuba.image.photopicker.util.BGAPhotoPickerUtil;

import java.util.ArrayList;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/28 上午11:09
 * 描述:
 */
public class BGAPhotoPickerAdapter extends BGARecyclerViewAdapter<String> {
    private ArrayList<String> mSelectedImages = new ArrayList<>();
    private int size;
    private boolean mTakePhotoEnabled;
    private Activity mActivity;
    /**
     * 裁剪模式
     */
    private boolean cropMode;

    public BGAPhotoPickerAdapter(Activity activity, RecyclerView recyclerView) {
        super(recyclerView, R.layout.bga_pp_item_photo_picker);
        size = BGAPhotoPickerUtil.getScreenWidth(recyclerView.getContext()) / 6;

        mActivity = activity;
    }

    @Override
    public void setItemChildListener(BGAViewHolderHelper helper, int viewType) {
        helper.setItemChildClickListener(R.id.iv_item_photo_picker_flag);
        helper.setItemChildClickListener(R.id.iv_item_photo_picker_photo);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, String model) {
        if (mTakePhotoEnabled && position == 0) {
            helper.setVisibility(R.id.tv_item_photo_picker_tip, View.VISIBLE);
            helper.getImageView(R.id.iv_item_photo_picker_photo).setScaleType(ImageView.ScaleType.CENTER);
            helper.setImageResource(R.id.iv_item_photo_picker_photo, R.mipmap.bga_pp_ic_gallery_camera);

            helper.setVisibility(R.id.iv_item_photo_picker_flag, View.INVISIBLE);
            helper.getImageView(R.id.iv_item_photo_picker_photo).setColorFilter(null);
        } else {
            helper.setVisibility(R.id.tv_item_photo_picker_tip, View.INVISIBLE);
            helper.getImageView(R.id.iv_item_photo_picker_photo).setScaleType(ImageView.ScaleType.CENTER_CROP);

            BGAImage.displayImage(mActivity,
                    helper.getImageView(R.id.iv_item_photo_picker_photo),
                    model,
                    R.mipmap.bga_pp_ic_holder_dark,
                    R.mipmap.bg_pic_black_break,
                    size,
                    size,
                    null);

            if (cropMode)
                helper.setVisibility(R.id.iv_item_photo_picker_flag, View.GONE);
            else
                helper.setVisibility(R.id.iv_item_photo_picker_flag, View.VISIBLE);

            if (mSelectedImages.contains(model)) {
                helper.setImageResource(R.id.iv_item_photo_picker_flag, R.mipmap.bga_pp_ic_cb_yellow_checked);
                helper.getImageView(R.id.iv_item_photo_picker_photo).setColorFilter(helper.getConvertView().getResources().getColor(R.color.bga_pp_photo_selected_mask));
            } else {
                helper.setImageResource(R.id.iv_item_photo_picker_flag, R.mipmap.bga_pp_ic_cb_normal);
                helper.getImageView(R.id.iv_item_photo_picker_photo).setColorFilter(null);
            }
        }
    }

    public void setSelectedImages(ArrayList<String> selectedImages) {
        if (selectedImages != null) {
            mSelectedImages = selectedImages;
        }
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedImages() {
        return mSelectedImages;
    }

    public int getSelectedCount() {
        return mSelectedImages.size();
    }

    public void setImageFolderModel(BGAImageFolderModel imageFolderModel) {
        mTakePhotoEnabled = imageFolderModel.isTakePhotoEnabled();
        setData(imageFolderModel.getImages());
    }

    public void hideSelectFlag() {
        cropMode = true;
        notifyDataSetChanged();
    }
}
