package com.wuba.image.photopicker.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.wuba.image.R;
import com.wuba.image.photopicker.PhotoPickerApi;
import com.wuba.image.photopicker.adapter.BGAPhotoPageAdapter;
import com.wuba.image.photopicker.imageloader.BGAImage;
import com.wuba.image.photopicker.imageloader.BGAImageLoader;
import com.wuba.image.photopicker.util.BGAAsyncTask;
import com.wuba.image.photopicker.util.BGAPhotoPickerUtil;
import com.wuba.image.photopicker.util.BGASavePhotoTask;
import com.wuba.image.photopicker.widget.BGAHackyViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.wuba.image.photopicker.activity.PhotoPickerConstant.EXTRA_CURRENT_POSITION;
import static com.wuba.image.photopicker.activity.PhotoPickerConstant.EXTRA_IS_SELF;
import static com.wuba.image.photopicker.activity.PhotoPickerConstant.EXTRA_IS_SINGLE_PREVIEW;
import static com.wuba.image.photopicker.activity.PhotoPickerConstant.EXTRA_PHOTO_PATH;
import static com.wuba.image.photopicker.activity.PhotoPickerConstant.EXTRA_PREVIEW_IMAGES;
import static com.wuba.image.photopicker.activity.PhotoPickerConstant.EXTRA_SAVE_IMG_DIR;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/24 下午2:59
 * 描述:单张大图 预览界面
 */
public class BGAPhotoPreviewActivity extends BGAPPToolbarActivity implements
        PhotoViewAttacher.OnViewTapListener, BGAAsyncTask.Callback<Void>,
        EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_CODE_PHOTO_CROP = 3;

    private static final int REQUEST_CODE_STORAGE_PHOTO = 101;

    private BGAHackyViewPager mContentHvp;
    private BGAPhotoPageAdapter mPhotoPageAdapter;

    private boolean mIsSinglePreview;

    private File mSaveImgDir;
    private boolean isSelf;

    private boolean mIsHidden = false;
    private BGASavePhotoTask mSavePhotoTask;

    /**
     * 上一次标题栏显示或隐藏的时间戳
     */
    private long mLastShowHiddenTime;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setNoLinearContentView(R.layout.bga_pp_activity_photo_preview);
        mContentHvp = getViewById(R.id.hvp_photo_preview_content);
    }

    @Override
    protected void setListener() {
        mContentHvp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                renderTitleTv();
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mSaveImgDir = (File) getIntent().getSerializableExtra(EXTRA_SAVE_IMG_DIR);
        if (mSaveImgDir != null && !mSaveImgDir.exists()) {
            mSaveImgDir.mkdirs();
        }

        ArrayList<String> previewImages = getIntent().getStringArrayListExtra(EXTRA_PREVIEW_IMAGES);
        isSelf = getIntent().getBooleanExtra(EXTRA_IS_SELF, false);

        mIsSinglePreview = getIntent().getBooleanExtra(EXTRA_IS_SINGLE_PREVIEW, false);
        if (mIsSinglePreview) {
            previewImages = new ArrayList<>();
            previewImages.add(getIntent().getStringExtra(EXTRA_PHOTO_PATH));
        }

        int currentPosition = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);

        mPhotoPageAdapter = new BGAPhotoPageAdapter(this, this, previewImages);
        mContentHvp.setAdapter(mPhotoPageAdapter);
        mContentHvp.setCurrentItem(currentPosition);

        // 过2秒隐藏标题栏
//        mToolbar.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hiddenTitlebar();
//            }
//        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bga_pp_menu_photo_preview, menu);
        MenuItem itemSelect = menu.findItem(R.id.item_photo_preview_select);
        MenuItem itemDownload = menu.findItem(R.id.item_photo_preview_download);

        //是自己才显示更换头像
        itemSelect.setVisible(isSelf);

        if (mSaveImgDir == null) {
            itemDownload.setVisible(false);
        }

        renderTitleTv();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int i = item.getItemId();
        if (i == R.id.item_photo_preview_select) {
            startActivityForResult(PhotoPickerApi.pickerIntent(this, true, true), REQUEST_CODE_PHOTO_CROP);//自己,修改头像
            return true;
        } else if (i == R.id.item_photo_preview_download) {
            if (mSavePhotoTask == null) {
                requestStoragePermission();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void renderTitleTv() {
        if (mPhotoPageAdapter == null) {
            return;
        }

        if (mIsSinglePreview) {
            mToolbar.setTitle(R.string.bga_pp_view_photo);
        } else {
            mToolbar.setTitle((mContentHvp.getCurrentItem() + 1) + "/" + mPhotoPageAdapter.getCount());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PHOTO_CROP) {
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        if (System.currentTimeMillis() - mLastShowHiddenTime > 500) {
            mLastShowHiddenTime = System.currentTimeMillis();
            if (mIsHidden) {
                showTitlebar();
            } else {
                hiddenTitlebar();
            }
        }
    }

    private void showTitlebar() {
        if (mToolbar != null) {
            ViewCompat.animate(mToolbar).translationY(0).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = false;
                }
            }).start();
        }
    }

    private void hiddenTitlebar() {
        if (mToolbar != null) {
            ViewCompat.animate(mToolbar).translationY(-mToolbar.getHeight()).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = true;
                }
            }).start();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_photo_preview_download) {
            if (mSavePhotoTask == null) {
                requestStoragePermission();
            }
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_STORAGE_PHOTO)
    private void requestStoragePermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            savePic();
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
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("权限申请")
                    .setRationale("您拒绝了 存储 权限，如想正常使用，请在设置中打开")
                    .build()
                    .show();
        }
    }


    private synchronized void savePic() {
        if (mSavePhotoTask != null) {
            return;
        }

        final String url = mPhotoPageAdapter.getItem(mContentHvp.getCurrentItem());
        File file;
        if (url.startsWith("file")) {
            file = new File(url.replace("file://", ""));
            if (file.exists()) {
                BGAPhotoPickerUtil.showSafe(BGAPhotoPreviewActivity.this, getString(R.string.bga_pp_save_img_success_folder, file.getParentFile().getAbsolutePath()));
                return;
            }
        }

        // 通过MD5加密url生成文件名，避免多次保存同一张图片
        file = new File(mSaveImgDir, BGAPhotoPickerUtil.md5(url) + BGAPhotoPickerUtil.getSuffix(url));
        if (file.exists()) {
            BGAPhotoPickerUtil.showSafe(this, getString(R.string.bga_pp_save_img_success_folder, mSaveImgDir.getAbsolutePath()));
            return;
        }

        mSavePhotoTask = new BGASavePhotoTask(this, this, file);
        BGAImage.downloadImage(this, url, new BGAImageLoader.DownloadDelegate() {

            @Override
            public void onSuccess(String path, byte[] resource) {
                mSavePhotoTask.setBitmapAndPerform(resource);
            }

            @Override
            public void onFailed(String url) {
                mSavePhotoTask = null;
                BGAPhotoPickerUtil.show(BGAPhotoPreviewActivity.this, R.string.bga_pp_save_img_failure);
            }
        });
    }

    private void savePicAndForward() {
        if (mSavePhotoTask != null) {
            return;
        }

        final String url = mPhotoPageAdapter.getItem(mContentHvp.getCurrentItem());
        File file;
        if (url.startsWith("file")) {
            file = new File(url.replace("file://", ""));
            if (file.exists()) {
                gotoForward(file.getAbsolutePath());
                return;
            }
        }

        // 通过MD5加密url生成文件名，避免多次保存同一张图片
        file = new File(mSaveImgDir, BGAPhotoPickerUtil.md5(url) + BGAPhotoPickerUtil.getSuffix(url));
        if (file.exists()) {
            gotoForward(file.getAbsolutePath());
            return;
        }

        mSavePhotoTask = new BGASavePhotoTask(this, this, file);
        BGAImage.downloadImage(this, url, new BGAImageLoader.DownloadDelegate() {

            @Override
            public void onSuccess(String path, byte[] resource) {
                mSavePhotoTask.setBitmapAndPerform(resource);
            }

            @Override
            public void onFailed(String url) {
                mSavePhotoTask = null;
                BGAPhotoPickerUtil.show(BGAPhotoPreviewActivity.this, R.string.bga_pp_save_img_failure);
            }
        });
    }

    private void gotoForward(String filePath) {
        Intent intent = new Intent("com.crm.wuba.forward");
        intent.putExtra("imagePath", filePath);
        startActivity(intent);
    }

    @Override
    public void onPostExecute(Void aVoid) {
        mSavePhotoTask = null;
    }

    @Override
    public void onTaskCancelled() {
        mSavePhotoTask = null;
    }

    @Override
    protected void onDestroy() {
        if (mSavePhotoTask != null) {
            mSavePhotoTask.cancelTask();
            mSavePhotoTask = null;
        }

        mContentHvp = null;
        mPhotoPageAdapter = null;
        mSaveImgDir = null;

        super.onDestroy();
    }
}