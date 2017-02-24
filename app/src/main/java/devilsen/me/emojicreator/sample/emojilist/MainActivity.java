package devilsen.me.emojicreator.sample.emojilist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.sample.BaseActivity;
import devilsen.me.emojicreator.sample.emojilist.imagelist.ImageListFragment;

public class MainActivity extends BaseActivity {

    private static String[] titles = new String[]{"手气不错", "热门", "我的表情", "全部", "分类"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);

        TabLayout mTableLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager_content);

        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        mTableLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
//            Intent intent = new Intent(MainActivity.this, SortActivity.class);
//            intent.putExtra("activity_name", "search");
//            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    static class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
//            if (position == 0) {
                return new ImageListFragment();
//            } else if (position == 1) {
//                return ImageListFragment.newInstance(Constant.LIST_TYPE_HOT);
//            } else if (position == 2) {
//                return new MyEmojiFragment();
//            } else if (position == 3) {
//                return ImageListFragment.newInstance(Constant.LIST_TYPE_DEFAULT);
//            }else{
//                return new ImageListFragment();
//            }
//            } else {
//                return new SortFragment();
//            }
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
