package devilsen.me.emojicreator.sample;

import android.support.v7.app.AppCompatActivity;

import devilsen.me.emojicreator.util.analyze.Umeng;

/**
 * author : dongSen
 * date : 2017-02-22 16:54
 * desc :
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        Umeng.fragmentActivityResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Umeng.fragmentActivityPause(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
