package devilsen.me.emojicreator.sample.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.data.ImageBean;
import devilsen.me.emojicreator.sample.BaseFragment;
import devilsen.me.emojicreator.sample.emojilist.imagelist.SourceListAdapter;
import devilsen.me.emojicreator.util.IntentUtil;
import devilsen.me.emojicreator.widget.SpacesItemDecoration;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * author : dongSen
 * date : 2017-03-02 15:43
 * desc :
 */
public class SearchEmojiFragment extends BaseFragment implements SearchEmojiContract.View {

    private SwipeRefreshLayout mSwipeRefresh;

    private RecyclerView mRecyclerView;
    private SourceListAdapter mAdapter;

    private StaggeredGridLayoutManager mLayoutManager;

    private View mEmptyView;

    private int page;

    private SearchEmojiContract.Presenter mPresenter;

    public static SearchEmojiFragment newInstance() {
        return new SearchEmojiFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container,false);
        mSwipeRefresh = (SwipeRefreshLayout) root.findViewById(R.id.search_refresh_layout);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.search_recycler);
        mEmptyView = root.findViewById(R.id.search_empty_layout);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SourceListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(12));
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        mSwipeRefresh.setOnRefreshListener(() -> loadImage(null, true));

        mAdapter.setItemClickListener(new SourceListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ImageBean bean, View imageView) {
                IntentUtil.startImg(mActivity, bean, imageView);
            }

            @Override
            public void onItemLongClick(ImageBean bean, int position) {
            }
        });

        /**
         * 在快速滑动时，不加载图片
         */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > 30) {
                    Glide.with(getContext().getApplicationContext()).pauseRequests();
                } else {
                    Glide.with(getContext().getApplicationContext()).resumeRequests();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(getContext().getApplicationContext()).resumeRequests();
                    onScroll();
                }
            }
        });
    }

    private void onScroll() {
        int[] positions = new int[mLayoutManager.getSpanCount()];
        mLayoutManager.findLastVisibleItemPositions(positions);

        for (int position : positions) {
            if (position == mLayoutManager.getItemCount() - 1) {
                page++;
                loadImage(null, false);
                break;
            }
        }
    }

    @Override
    public void showEmojis(List<ImageBean> listData) {
        if (page == 0) {
            mAdapter.replaceData(listData);
        } else {
            mAdapter.addData(listData);
        }

        mSwipeRefresh.setRefreshing(false);
    }

    String keyword;

    @Override
    public void loadImage(@Nullable String keyword, boolean isFresh) {
        if (keyword != null)
            this.keyword = keyword;

        if (isFresh) {
            if (mSwipeRefresh != null)
                mSwipeRefresh.setRefreshing(true);
            page = 0;
        }

        if (mPresenter != null)
            mPresenter.loadEmojis(this.keyword, page);
    }

    @Override
    public void stopFresh() {
        if (mSwipeRefresh != null)
            mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void setPresenter(@NonNull SearchEmojiContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

}
