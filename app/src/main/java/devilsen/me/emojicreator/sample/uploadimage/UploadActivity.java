package devilsen.me.emojicreator.sample.uploadimage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wuba.image.photopicker.PhotoPickerApi;
import com.wuba.image.photopicker.activity.BGAPhotoPickerActivity;

import java.util.ArrayList;

import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.sample.BaseActivity;
import devilsen.me.emojicreator.util.ActivityUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * author : dongSen
 * date : 2017/3/16 下午4:06
 * desc : 上传图片
 */
public class UploadActivity extends BaseActivity implements UploadImageContract.View {

    private static final int REQUEST_CODE_IMAGE = 1;

    private ImageView mImage;

    private TextView mTitleTxt;

    private UploadImageContract.Presenter mPresenter;

    private String mImagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ActivityUtils.initToolbar(this);

        getSupportActionBar().setTitle(R.string.share_your_emoji);

        mImage = (ImageView) findViewById(R.id.img_upload);
        mTitleTxt = (TextView) findViewById(R.id.edit_upload_name);

        mImage.setOnClickListener(v -> changeImage());

        mPresenter = new UploadPresenter(this, this);

        changeImage();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_upload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_upload) {
            uploadImage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(UploadImageContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void loadImage(String imagePath) {
        this.mImagePath = imagePath;
        mPresenter.loadImage(this, mImage, imagePath);
    }

    @Override
    public void uploadImage() {
        if (checkImageAndName())
            mPresenter.uploadImage(mImagePath, mTitleTxt.getText().toString());
    }

    private boolean checkImageAndName() {
        if (mImagePath == null || TextUtils.isEmpty(mImagePath)) {
            Toast.makeText(this, getString(R.string.toast_no_select_image), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mTitleTxt.getText().toString().trim())) {
            Toast.makeText(this, getString(R.string.toast_no_input_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void changeImage() {
        startActivityForResult(PhotoPickerApi.pickerIntent(this), REQUEST_CODE_IMAGE);
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            ArrayList<String> selectedImages = BGAPhotoPickerActivity.getSelectedImages(data);
            if (selectedImages != null && selectedImages.size() > 0) {
                loadImage(selectedImages.get(0));
            }
        }
    }
}
