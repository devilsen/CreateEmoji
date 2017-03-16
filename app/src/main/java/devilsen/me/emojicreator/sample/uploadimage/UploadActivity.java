package devilsen.me.emojicreator.sample.uploadimage;

import android.os.Bundle;
import android.support.annotation.Nullable;

import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.sample.BaseActivity;
import devilsen.me.emojicreator.util.ActivityUtils;

/**
 * author : dongSen
 * date : 2017/3/16 下午4:06
 * desc : 上传图片
 */
public class UploadActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ActivityUtils.initToolbar(this);

        getSupportActionBar().setTitle(R.string.share_your_emoji);
    }
}
