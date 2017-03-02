package devilsen.me.emojicreator.sample.other;

import android.os.Bundle;

import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.sample.BaseActivity;
import devilsen.me.emojicreator.util.ActivityUtils;


/**
 * Description : 反馈界面
 * author : dongsen
 * Time : 2016-03-30
 */
public class FeedbackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ActivityUtils.initToolbar(this);

    }
}
