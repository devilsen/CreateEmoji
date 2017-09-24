package com.wuba.image.photopicker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuba.image.R;
import com.wuba.image.photopicker.adapter.BGAPhotoPageAdapter;
import com.wuba.image.photopicker.util.BGAPhotoPickerUtil;
import com.wuba.image.photopicker.widget.BGAHackyViewPager;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

import static com.wuba.image.photopicker.activity.PhotoPickerConstant.EXTRA_CURRENT_POSITION;
import static com.wuba.image.photopicker.activity.PhotoPickerConstant.EXTRA_IS_FROM_TAKE_PHOTO;
import static com.wuba.image.photopicker.activity.PhotoPickerConstant.EXTRA_MAX_CHOOSE_COUNT;
import static com.wuba.image.photopicker.activity.PhotoPickerConstant.EXTRA_PREVIEW_IMAGES;
import static com.wuba.image.photopicker.activity.PhotoPickerConstant.EXTRA_SELECTED_IMAGES;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/24 下午2:57
 * 描述:单张大图 预览并选择 界面
 */
public class BGAPhotoPickerPreviewActivity extends BGAPPToolbarActivity implements PhotoViewAttacher.OnViewTapListener {

    private TextView mTitleTv;
    private TextView mSubmitTv;
    private BGAHackyViewPager mContentHvp;
    private RelativeLayout mChooseRl;
    private TextView mChooseTv;

    private ArrayList<String> mSelectedImages;
    private BGAPhotoPageAdapter mPhotoPageAdapter;
    private int mMaxChooseCount = 1;
    /**
     * 右上角按钮文本
     */
    private String mTopRightBtnText;

    private boolean mIsHidden = false;
    /**
     * 上一次标题栏显示或隐藏的时间戳
     */
    private long mLastShowHiddenTime;
    /**
     * 是否是拍完照后跳转过来
     */
    private boolean mIsFromTakePhoto;


    /**
     * 获取已选择的图片集合
     *
     * @param intent
     * @return
     */
    public static ArrayList<String> getSelectedImages(Intent intent) {
        return intent.getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
    }

    /**
     * 是否是拍照预览
     *
     * @param intent
     * @return
     */
    public static boolean getIsFromTakePhoto(Intent intent) {
        return intent.getBooleanExtra(EXTRA_IS_FROM_TAKE_PHOTO, false);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setNoLinearContentView(R.layout.bga_pp_activity_photo_picker_preview);
        mContentHvp = getViewById(R.id.hvp_photo_picker_preview_content);
        mChooseRl = getViewById(R.id.rl_photo_picker_preview_choose);
        mChooseTv = getViewById(R.id.tv_photo_picker_preview_choose);
    }

    @Override
    protected void setListener() {
        mChooseTv.setOnClickListener(this);

        mContentHvp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                handlePageSelectedStatus();
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // 获取图片选择的最大张数
        mMaxChooseCount = getIntent().getIntExtra(EXTRA_MAX_CHOOSE_COUNT, 1);
        if (mMaxChooseCount < 1) {
            mMaxChooseCount = 1;
        }

        mSelectedImages = getIntent().getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
        ArrayList<String> previewImages = getIntent().getStringArrayListExtra(EXTRA_PREVIEW_IMAGES);
        if (TextUtils.isEmpty(previewImages.get(0))) {
            // 从BGAPhotoPickerActivity跳转过来时，如果有开启拍照功能，则第0项为""
            previewImages.remove(0);
        }

        // 处理是否是拍完照后跳转过来
        mIsFromTakePhoto = getIntent().getBooleanExtra(EXTRA_IS_FROM_TAKE_PHOTO, false);
        if (mIsFromTakePhoto) {
            // 如果是拍完照后跳转过来，一直隐藏底部选择栏
            mChooseRl.setVisibility(View.INVISIBLE);
        }
        int currentPosition = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);

        // 获取右上角按钮文本
        mTopRightBtnText = getString(R.string.bga_pp_confirm);


        mPhotoPageAdapter = new BGAPhotoPageAdapter(this, this, previewImages);
        mContentHvp.setAdapter(mPhotoPageAdapter);
        mContentHvp.setCurrentItem(currentPosition);


        // 过2秒隐藏标题栏和底部选择栏
        mToolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                hiddenToolbarAndChoosebar();
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bga_pp_menu_photo_picker_preview, menu);
        MenuItem menuItem = menu.findItem(R.id.item_photo_picker_preview_title);
        View actionView = menuItem.getActionView();

        mTitleTv = (TextView) actionView.findViewById(R.id.tv_photo_picker_preview_title);
        mSubmitTv = (TextView) actionView.findViewById(R.id.tv_photo_picker_preview_submit);
        mSubmitTv.setOnClickListener(this);

        renderTopRightBtn();
        handlePageSelectedStatus();

        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_photo_picker_preview_submit) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, mSelectedImages);
            intent.putExtra(EXTRA_IS_FROM_TAKE_PHOTO, mIsFromTakePhoto);
            setResult(RESULT_OK, intent);
            finish();
        } else if (v.getId() == R.id.tv_photo_picker_preview_choose) {
            String currentImage = mPhotoPageAdapter.getItem(mContentHvp.getCurrentItem());
            if (mSelectedImages.contains(currentImage)) {
                mSelectedImages.remove(currentImage);
                mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bga_pp_ic_cb_normal, 0, 0, 0);
                renderTopRightBtn();
            } else {
                if (mMaxChooseCount == 1) {
                    // 单选

                    mSelectedImages.clear();
                    mSelectedImages.add(currentImage);
                    mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bga_pp_ic_cb_yellow_checked, 0, 0, 0);
                    renderTopRightBtn();
                } else {
                    // 多选

                    if (mMaxChooseCount == mSelectedImages.size()) {
                        BGAPhotoPickerUtil.show(this, getString(R.string.bga_pp_toast_photo_picker_max, mMaxChooseCount));
                    } else {
                        mSelectedImages.add(currentImage);
                        mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bga_pp_ic_cb_yellow_checked, 0, 0, 0);
                        renderTopRightBtn();
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, mSelectedImages);
        intent.putExtra(EXTRA_IS_FROM_TAKE_PHOTO, mIsFromTakePhoto);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private void handlePageSelectedStatus() {
        if (mTitleTv == null || mPhotoPageAdapter == null) {
            return;
        }

        mTitleTv.setText((mContentHvp.getCurrentItem() + 1) + "/" + mPhotoPageAdapter.getCount());
        if (mSelectedImages.contains(mPhotoPageAdapter.getItem(mContentHvp.getCurrentItem()))) {
            mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bga_pp_ic_cb_yellow_checked, 0, 0, 0);
        } else {
            mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bga_pp_ic_cb_normal, 0, 0, 0);
        }
    }

    /**
     * 渲染右上角按钮
     */
    private void renderTopRightBtn() {
        if (mIsFromTakePhoto) {
            mSubmitTv.setEnabled(true);
            mSubmitTv.setText(mTopRightBtnText);
        } else if (mSelectedImages.size() == 0) {
            mSubmitTv.setEnabled(false);
            mSubmitTv.setText(mTopRightBtnText);
        } else {
            mSubmitTv.setEnabled(true);
            mSubmitTv.setText(mTopRightBtnText + "(" + mSelectedImages.size() + "/" + mMaxChooseCount + ")");
        }
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        if (System.currentTimeMillis() - mLastShowHiddenTime > 500) {
            mLastShowHiddenTime = System.currentTimeMillis();
            if (mIsHidden) {
                showTitlebarAndChoosebar();
            } else {
                hiddenToolbarAndChoosebar();
            }
        }
    }

    @Override
    protected void onDestroy() {
        mTitleTv = null;
        mSubmitTv = null;
        mContentHvp = null;
        mChooseRl = null;
        mChooseTv = null;

        mSelectedImages = null;
        mPhotoPageAdapter = null;

        super.onDestroy();
    }

    private void showTitlebarAndChoosebar() {
        if (mToolbar != null) {
            ViewCompat.animate(mToolbar).translationY(0).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = false;
                }
            }).start();
        }

        if (!mIsFromTakePhoto && mChooseRl != null) {
            mChooseRl.setVisibility(View.VISIBLE);
            ViewCompat.setAlpha(mChooseRl, 0);
            ViewCompat.animate(mChooseRl).alpha(1).setInterpolator(new DecelerateInterpolator(2)).start();
        }
    }

    private void hiddenToolbarAndChoosebar() {
        if (mToolbar != null) {
            ViewCompat.animate(mToolbar).translationY(-mToolbar.getHeight()).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = true;
                    if (mChooseRl != null) {
                        mChooseRl.setVisibility(View.INVISIBLE);
                    }
                }
            }).start();
        }

        if (!mIsFromTakePhoto) {
            if (mChooseRl != null) {
                ViewCompat.animate(mChooseRl).alpha(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }
        }
    }

}