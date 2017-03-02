package devilsen.me.emojicreator.sample.search;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import devilsen.me.emojicreator.Injection;
import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.sample.BaseActivity;
import devilsen.me.emojicreator.util.ActivityUtils;
import devilsen.me.emojicreator.util.KeyboardUtil;
import devilsen.me.emojicreator.util.analyze.Umeng;

/**
 * Description :  分类结果activity
 * author : dongsen
 * Time : 2016-03-01
 */
public class SearchEmojiActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private EditText searchEdit;

    private SearchEmojiFragment mSearchEmojiFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_emoji_list);

        initTitle();
    }

    private void initTitle() {
        searchEdit = (EditText) findViewById(R.id.search_edit);
        ImageButton searchBtn = (ImageButton) findViewById(R.id.search_btn);

        ActivityUtils.initToolbar(this);

        searchBtn.setOnClickListener(this);
        searchEdit.setOnEditorActionListener(this);

        mSearchEmojiFragment =
                (SearchEmojiFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (mSearchEmojiFragment == null) {
            mSearchEmojiFragment = SearchEmojiFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mSearchEmojiFragment, R.id.contentFrame);
        }

        SearchEmojiContract.Presenter mSearchPresenter = new SearchEmojiPresenter(Injection.provideEmojiRepository(this),
                mSearchEmojiFragment, Injection.provideSchedulersProvider());

        mSearchEmojiFragment.setPresenter(mSearchPresenter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn:
                doSearch();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            doSearch();
            return true;
        }
        return false;
    }

    /**
     * 搜索
     */
    private void doSearch() {
        String keyword = searchEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(keyword)) {
            mSearchEmojiFragment.loadImage(keyword, true);

            KeyboardUtil.hideSoftInput(this, searchEdit);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Umeng.activityResume(this, "search_activity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Umeng.activityPause(this, "search_activity");
    }
}
