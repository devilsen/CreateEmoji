package devilsen.me.emojicreator.sample.other;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.sample.BaseActivity;
import devilsen.me.emojicreator.util.ActivityUtils;


/**
 * Description : 关于界面
 * author : dongsen
 * Time : 2016-03-30
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private Button checkUpdateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                AppCompatDelegate delegate = getDelegate();
                View view = delegate.createView(parent, name, context, attrs);
                //进行处理

                return view;
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActivityUtils.initToolbar(this);

        checkUpdateBtn = (Button) findViewById(R.id.check_update);
        checkUpdateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
