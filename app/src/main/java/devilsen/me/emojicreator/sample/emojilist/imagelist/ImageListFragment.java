package devilsen.me.emojicreator.sample.emojilist.imagelist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import devilsen.me.emojicreator.Constant;
import devilsen.me.emojicreator.Injection;
import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.data.ImageBean;
import devilsen.me.emojicreator.sample.BaseFragment;
import devilsen.me.emojicreator.widget.SpacesItemDecoration;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * author : dongSen
 * date : 2017-02-22 17:05
 * desc : 图片列表
 */
public class ImageListFragment extends BaseFragment implements ListContract.View {

    private ListContract.Presenter mPresenter;

    private RecyclerView emojiRecyclerView;
    private SourceListAdapter mAdapter;

    private SwipeRefreshLayout emojiRefreshLayout;
    private LinearLayout emptyLayout;
    private FloatingActionButton fab;

    private int type = Constant.TYPE_LUCK;
    private int page;

    private boolean firstVisible = true;

    public static Fragment newInstance(int type) {
        ImageListFragment imageListFragment = new ImageListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BUNDLE_TYPE, type);
        imageListFragment.setArguments(bundle);
        return imageListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_good_luck, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null)
            type = getArguments().getInt(Constant.BUNDLE_TYPE, Constant.TYPE_LUCK);

        emojiRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.list_source_sr);
        emojiRecyclerView = (RecyclerView) view.findViewById(R.id.list_source_rv);
        emptyLayout = (LinearLayout) view.findViewById(R.id.empty_layout);
        fab = (FloatingActionButton) view.findViewById(R.id.goodluck_fab);

        mAdapter = new SourceListAdapter(new ArrayList<>());
        emojiRecyclerView.setAdapter(mAdapter);
        emojiRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        emojiRecyclerView.addItemDecoration(new SpacesItemDecoration(12));

        emojiRefreshLayout.setOnRefreshListener(() -> loadImage(true));

        mPresenter = new ListPresenter(Injection.provideEmojiRepository(getContext()),
                this,
                Injection.provideSchedulersProvider());

        /**
         * 在快速滑动时，不加载图片
         */
        emojiRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > 30) {
                    Glide.with(getContext()).pauseRequests();
                } else {
                    Glide.with(getContext()).resumeRequests();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(getContext()).resumeRequests();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unSubscribe();
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showEmojis(List<ImageBean> listData) {
        mAdapter.replaceData(listData);
        emojiRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadImage(boolean isFresh) {
        mPresenter.loadList(type, 0);
    }

    @Override
    public void createEmoji(ImageBean imageBean) {

    }

    @Override
    public void setPresenter(ListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
