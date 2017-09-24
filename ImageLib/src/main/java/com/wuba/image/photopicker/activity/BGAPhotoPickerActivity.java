package com.wuba.image.photopicker.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuba.image.R;
import com.wuba.image.photopicker.PhotoPickerApi;
import com.wuba.image.photopicker.adapter.BGAPhotoPickerAdapter;
import com.wuba.image.photopicker.crop.Crop;
import com.wuba.image.photopicker.imageloader.BGAImage;
import com.wuba.image.photopicker.model.BGAImageFolderModel;
import com.wuba.image.photopicker.pw.BGAPhotoFolderPw;
import com.wuba.image.photopicker.util.BGAAsyncTask;
import com.wuba.image.photopicker.util.BGAImageCaptureManager;
import com.wuba.image.photopicker.util.BGALoadPhotoTask;
import com.wuba.image.photopicker.util.BGAPhotoPickerUtil;
import com.wuba.image.photopicker.util.BGASpaceItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.wuba.image.photopicker.activity.PhotoPickerConstant.EXTRA_CROP_MODE;
import static com.wuba.image.photopicker.activity.PhotoPickerConstant.EXTRA_IMAGE_DIR;
import static com.wuba.image.photopicker.activity.PhotoPickerConstant.EXTRA_MAX_CHOOSE_COUNT;
import static com.wuba.image.photopicker.activity.PhotoPickerConstant.EXTRA_SELECTED_IMAGES;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/24 下午2:55
 * 描述:图片选择界面
 */
public class BGAPhotoPickerActivity extends BGAPPToolbarActivity implements
        BGAOnItemChildClickListener, BGAAsyncTask.Callback<ArrayList<BGAImageFolderModel>>,
        EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_CODE_PERMISSION_TAKE_PHOTO = 100;
    private static final int REQUEST_CODE_STORAGE_PHOTO = 101;

    /**
     * 拍照的请求码
     */
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    /**
     * 预览照片的请求码
     */
    private static final int REQUEST_CODE_PREVIEW = 2;

    /**
     * 裁剪图片请求码
     */
    private static final int REQUEST_CODE_CROP = 6709;

    private TextView mTitleTv;
    private ImageView mArrowIv;
    private TextView mSubmitTv;
    private RecyclerView mContentRv;

    private BGAImageFolderModel mCurrentImageFolderModel;

    /**
     * 是否可以拍照
     */
    private boolean mTakePhotoEnabled;
    /**
     * 最多选择多少张图片，默认等于1，为单选
     */
    private int mMaxChooseCount = 1;
    /**
     * 右上角按钮文本
     */
    private String mTopRightBtnText;
    /**
     * 图片目录数据集合
     */
    private ArrayList<BGAImageFolderModel> mImageFolderModels;

    private BGAPhotoPickerAdapter mPicAdapter;

    private BGAImageCaptureManager mImageCaptureManager;

    private BGAPhotoFolderPw mPhotoFolderPw;
    /**
     * 上一次显示图片目录的时间戳，防止短时间内重复点击图片目录菜单时界面错乱
     */
    private long mLastShowPhotoFolderTime;
    private BGALoadPhotoTask mLoadPhotoTask;
    private AppCompatDialog mLoadingDialog;

    /**
     * 裁剪模式
     */
    private boolean cropMode;

    /**
     * 获取已选择的图片集合
     *
     * @param intent
     * @return
     */
    public static ArrayList<String> getSelectedImages(Intent intent) {
        return intent.getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.bga_pp_activity_photo_picker);
        mContentRv = getViewById(R.id.rv_photo_picker_content);
    }

    @Override
    protected void setListener() {
        mPicAdapter = new BGAPhotoPickerAdapter(this, mContentRv);
        mPicAdapter.setOnItemChildClickListener(this);

        mContentRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    BGAImage.resume(BGAPhotoPickerActivity.this);
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    BGAImage.pause(BGAPhotoPickerActivity.this);
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // 获取拍照图片保存目录
        File imageDir = (File) getIntent().getSerializableExtra(EXTRA_IMAGE_DIR);
        if (imageDir != null) {
            mTakePhotoEnabled = true;
            mImageCaptureManager = new BGAImageCaptureManager(this, imageDir);
        }
        // 获取图片选择的最大张数
        mMaxChooseCount = getIntent().getIntExtra(EXTRA_MAX_CHOOSE_COUNT, 1);
        if (mMaxChooseCount < 1) {
            mMaxChooseCount = 1;
        }

        cropMode = getIntent().getBooleanExtra(EXTRA_CROP_MODE, false);
        if (cropMode) {
            mMaxChooseCount = 1;
            mPicAdapter.hideSelectFlag();
        }

        // 获取右上角按钮文本
        mTopRightBtnText = getString(R.string.bga_pp_confirm);

        GridLayoutManager layoutManager = new GridLayoutManager(this, BGASpaceItemDecoration.SPAN_COUNT, LinearLayoutManager.VERTICAL, false);
        mContentRv.setLayoutManager(layoutManager);
        mContentRv.addItemDecoration(new BGASpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.bga_pp_size_photo_divider)));

        ArrayList<String> selectedImages = getIntent().getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
        if (selectedImages != null && selectedImages.size() > mMaxChooseCount) {
            String selectedPhoto = selectedImages.get(0);
            selectedImages.clear();
            selectedImages.add(selectedPhoto);
        }

        mContentRv.setAdapter(mPicAdapter);
        mPicAdapter.setSelectedImages(selectedImages);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestStoragePermission();
    }

    private void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new AppCompatDialog(this);
            mLoadingDialog.setContentView(R.layout.bga_pp_dialog_loading);
            mLoadingDialog.setCancelable(false);
        }
        mLoadingDialog.show();
    }

    private void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bga_pp_menu_photo_picker, menu);
        MenuItem menuItem = menu.findItem(R.id.item_photo_picker_title);
        View actionView = menuItem.getActionView();

        mTitleTv = (TextView) actionView.findViewById(R.id.tv_photo_picker_title);
        mArrowIv = (ImageView) actionView.findViewById(R.id.iv_photo_picker_arrow);
        mSubmitTv = (TextView) actionView.findViewById(R.id.tv_photo_picker_submit);

        mTitleTv.setOnClickListener(this);
        mArrowIv.setOnClickListener(this);
        mSubmitTv.setOnClickListener(this);

        mTitleTv.setText(R.string.bga_pp_all_image);
        if (mCurrentImageFolderModel != null) {
            mTitleTv.setText(mCurrentImageFolderModel.name);
        }

        renderTopRightBtn();

        return true;
    }

    @Override
    public void onClick(View v) {
        if ((v.getId() == R.id.tv_photo_picker_title || v.getId() == R.id.iv_photo_picker_arrow) && mImageFolderModels != null && mImageFolderModels.size() > 0 && System.currentTimeMillis() - mLastShowPhotoFolderTime > BGAPhotoFolderPw.ANIM_DURATION) {
            showPhotoFolderPw();
            mLastShowPhotoFolderTime = System.currentTimeMillis();
        } else if (v.getId() == R.id.tv_photo_picker_submit) {
            returnSelectedImages(mPicAdapter.getSelectedImages());
        }
    }

    /**
     * 返回已选中的图片集合
     *
     * @param selectedImages
     */
    private void returnSelectedImages(ArrayList<String> selectedImages) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, selectedImages);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showPhotoFolderPw() {
        if (mPhotoFolderPw == null) {
            mPhotoFolderPw = new BGAPhotoFolderPw(this, mToolbar, new BGAPhotoFolderPw.Delegate() {
                @Override
                public void onSelectedFolder(int position) {
                    reloadPhotos(position);
                }

                @Override
                public void executeDismissAnim() {
                    ViewCompat.animate(mArrowIv).setDuration(BGAPhotoFolderPw.ANIM_DURATION).rotation(0).start();
                }
            });
        }
        mPhotoFolderPw.setData(mImageFolderModels);
        mPhotoFolderPw.show();

        ViewCompat.animate(mArrowIv).setDuration(BGAPhotoFolderPw.ANIM_DURATION).rotation(-180).start();
    }

    /**
     * 显示只能选择 mMaxChooseCount 张图的提示
     */
    private void toastMaxCountTip() {
        BGAPhotoPickerUtil.show(this, getString(R.string.bga_pp_toast_photo_picker_max, mMaxChooseCount));
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        requestTakePhotoPermission();
    }


    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_TAKE_PHOTO)
    private void requestTakePhotoPermission() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            try {
                startActivityForResult(mImageCaptureManager.getTakePictureIntent(), REQUEST_CODE_TAKE_PHOTO);
            } catch (Exception e) {
                BGAPhotoPickerUtil.show(this, R.string.bga_pp_photo_not_support);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(perms, REQUEST_CODE_PERMISSION_TAKE_PHOTO);
            }
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_STORAGE_PHOTO)
    private void requestStoragePermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            showLoadingDialog();
            mLoadPhotoTask = new BGALoadPhotoTask(this, this, mTakePhotoEnabled).perform();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(perms, REQUEST_CODE_STORAGE_PHOTO);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        String text = "";

        if (requestCode == REQUEST_CODE_STORAGE_PHOTO) {
            text = "存储";
        } else if (requestCode == REQUEST_CODE_PERMISSION_TAKE_PHOTO) {
            text = "拍照";
        }

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("权限申请")
                    .setRationale("您拒绝了 " + text + " 权限，如想正常使用，请在设置中打开")
                    .build()
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                ArrayList<String> photos = new ArrayList<>();
                photos.add(mImageCaptureManager.getCurrentPhotoPath());
                if (cropMode) {
                    Uri source = Uri.fromFile(new File(photos.get(0)));
                    Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
                    Crop.of(source, destination).asSquare().start(this);
                } else {
                    startActivityForResult(
                            PhotoPickerApi.pickPreviewIntent(this, 1, photos, photos, 0, true),
                            REQUEST_CODE_PREVIEW);
                }
            } else if (requestCode == REQUEST_CODE_PREVIEW) {
                if (BGAPhotoPickerPreviewActivity.getIsFromTakePhoto(data)) {
                    // 从拍照预览界面返回，刷新图库
                    mImageCaptureManager.refreshGallery();
                }

                returnSelectedImages(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
            } else if (requestCode == REQUEST_CODE_CROP) {
                setResult(RESULT_OK, data);
                finish();
            }
        } else if (resultCode == RESULT_CANCELED && requestCode == REQUEST_CODE_PREVIEW) {
            if (BGAPhotoPickerPreviewActivity.getIsFromTakePhoto(data)) {
                // 从拍照预览界面返回，删除之前拍的照片
                mImageCaptureManager.deletePhotoFile();
            } else {
                mPicAdapter.setSelectedImages(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
                renderTopRightBtn();
            }
        }
    }

    /**
     * 渲染右上角按钮
     */
    private void renderTopRightBtn() {
        if (mPicAdapter.getSelectedCount() == 0) {
            mSubmitTv.setEnabled(false);
            mSubmitTv.setText(mTopRightBtnText);
        } else {
            mSubmitTv.setEnabled(true);
            mSubmitTv.setText(mTopRightBtnText + "(" + mPicAdapter.getSelectedCount() + "/" + mMaxChooseCount + ")");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mTakePhotoEnabled) {
            mImageCaptureManager.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (mTakePhotoEnabled) {
            mImageCaptureManager.onRestoreInstanceState(savedInstanceState);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onItemChildClick(ViewGroup viewGroup, View view, int position) {
        if (view.getId() == R.id.iv_item_photo_picker_flag) {
            handleClickSelectFlagIv(position);
        } else if (view.getId() == R.id.iv_item_photo_picker_photo) {
            handleClickPreviewIv(position);
        }
    }

    /**
     * 处理点击选择按钮事件
     *
     * @param position 当前点击的item的索引位置
     */
    private void handleClickSelectFlagIv(int position) {
        String currentImage = mPicAdapter.getItem(position);
        if (mMaxChooseCount == 1) {
            // 单选

            if (mPicAdapter.getSelectedCount() > 0) {
                String selectedImage = mPicAdapter.getSelectedImages().remove(0);
                if (TextUtils.equals(selectedImage, currentImage)) {
                    mPicAdapter.notifyItemChanged(position);
                } else {
                    int preSelectedImagePosition = mPicAdapter.getData().indexOf(selectedImage);
                    mPicAdapter.notifyItemChanged(preSelectedImagePosition);
                    mPicAdapter.getSelectedImages().add(currentImage);
                    mPicAdapter.notifyItemChanged(position);
                }
            } else {
                mPicAdapter.getSelectedImages().add(currentImage);
                mPicAdapter.notifyItemChanged(position);
            }
            renderTopRightBtn();
        } else {
            // 多选

            if (!mPicAdapter.getSelectedImages().contains(currentImage) && mPicAdapter.getSelectedCount() == mMaxChooseCount) {
                toastMaxCountTip();
            } else {
                if (mPicAdapter.getSelectedImages().contains(currentImage)) {
                    mPicAdapter.getSelectedImages().remove(currentImage);
                } else {
                    mPicAdapter.getSelectedImages().add(currentImage);
                }
                mPicAdapter.notifyItemChanged(position);

                renderTopRightBtn();
            }
        }
    }

    /**
     * 处理点击预览按钮事件
     *
     * @param position 当前点击的item的索引位置
     */
    private void handleClickPreviewIv(int position) {
        if (mMaxChooseCount == 1) {
            // 单选

            if (mCurrentImageFolderModel.isTakePhotoEnabled() && position == 0) {
                takePhoto();
            } else {
                if (cropMode)
                    cropImage(position);
                else
                    changeToPreview(position);
            }
        } else {
            // 多选

            if (mCurrentImageFolderModel.isTakePhotoEnabled() && position == 0) {
                if (mPicAdapter.getSelectedCount() == mMaxChooseCount) {
                    toastMaxCountTip();
                } else {
                    takePhoto();
                }
            } else {
                changeToPreview(position);
            }
        }
    }

    /**
     * 跳转到图片选择预览界面
     *
     * @param position 当前点击的item的索引位置
     */
    private void changeToPreview(int position) {
        int currentPosition = position;
        if (mCurrentImageFolderModel.isTakePhotoEnabled()) {
            currentPosition--;
        }
        startActivityForResult(
                PhotoPickerApi.pickPreviewIntent(this, mMaxChooseCount, mPicAdapter.getSelectedImages(), (ArrayList<String>) mPicAdapter.getData(), currentPosition, false),
                REQUEST_CODE_PREVIEW);
    }

    /**
     * 剪裁图片界面
     */
    private void cropImage(int position) {
        String currentImage = mPicAdapter.getItem(position);

        Uri source = Uri.fromFile(new File(currentImage));
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void reloadPhotos(int position) {
        if (position < mImageFolderModels.size()) {
            mCurrentImageFolderModel = mImageFolderModels.get(position);
            if (mTitleTv != null) {
                mTitleTv.setText(mCurrentImageFolderModel.name);
            }

            mPicAdapter.setImageFolderModel(mCurrentImageFolderModel);
        }
    }

    @Override
    public void onPostExecute(ArrayList<BGAImageFolderModel> imageFolderModels) {
        dismissLoadingDialog();
        mLoadPhotoTask = null;
        mImageFolderModels = imageFolderModels;
        reloadPhotos(mPhotoFolderPw == null ? 0 : mPhotoFolderPw.getCurrentPosition());
    }

    @Override
    public void onTaskCancelled() {
        dismissLoadingDialog();
        mLoadPhotoTask = null;
    }

    private void cancelLoadPhotoTask() {
        if (mLoadPhotoTask != null) {
            mLoadPhotoTask.cancelTask();
            mLoadPhotoTask = null;
        }
    }

    @Override
    protected void onDestroy() {
        dismissLoadingDialog();
        cancelLoadPhotoTask();

        mTitleTv = null;
        mArrowIv = null;
        mSubmitTv = null;
        mContentRv = null;
        mCurrentImageFolderModel = null;
        mTopRightBtnText = null;
        mImageFolderModels = null;
        mPicAdapter = null;
        mImageCaptureManager = null;
        mPhotoFolderPw = null;

        super.onDestroy();
    }
}