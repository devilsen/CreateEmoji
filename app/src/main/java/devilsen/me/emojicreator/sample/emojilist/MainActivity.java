package devilsen.me.emojicreator.sample.emojilist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import devilsen.me.emojicreator.Constant;
import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.sample.BaseActivity;
import devilsen.me.emojicreator.sample.emojilist.imagelist.ImageListFragment;
import devilsen.me.emojicreator.sample.emojilist.uploadlist.UploadListFragment;
import devilsen.me.emojicreator.sample.other.AboutActivity;
import devilsen.me.emojicreator.sample.other.FeedbackActivity;
import devilsen.me.emojicreator.sample.search.SearchEmojiActivity;
import devilsen.me.emojicreator.sample.uploadimage.UploadActivity;

public class MainActivity extends BaseActivity {

    private static String[] titles = new String[]{"手气不错", "新品发售", "热门搞笑", "个人制造", "上传列表"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        TabLayout mTableLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager_content);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_fresh);

        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        mTableLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0 && !floatingActionButton.isShown()) {
                    floatingActionButton.show();
                } else if (floatingActionButton.isShown()) {
                    floatingActionButton.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.menu_search:
                intent.setClass(this, SearchEmojiActivity.class);
                break;
            case R.id.menu_feedback:
                intent.setClass(this, FeedbackActivity.class);
                break;
            case R.id.menu_about:
                intent.setClass(this, AboutActivity.class);
                break;
            case R.id.menu_upload:
                intent.setClass(this, UploadActivity.class);
                break;
        }
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return ImageListFragment.newInstance(Constant.TYPE_LUCK);
            } else if (position == 1) {
                return ImageListFragment.newInstance(Constant.TYPE_ALL);
            } else if (position == 2) {
                return ImageListFragment.newInstance(Constant.TYPE_HOT);
            } else if (position == 3) {
                return ImageListFragment.newInstance(Constant.TYPE_LOCAL);
            } else if (position == 4) {
                return UploadListFragment.newInstance();
            } else {
                return ImageListFragment.newInstance(Constant.TYPE_LOCAL);
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }
}
