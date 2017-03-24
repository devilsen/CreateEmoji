package devilsen.me.emojicreator.sample.emojilist.imagelist;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import java.util.List;

import devilsen.me.emojicreator.Constant;
import devilsen.me.emojicreator.Injection;
import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.data.ImageBean;
import devilsen.me.emojicreator.sample.BaseFragment;
import devilsen.me.emojicreator.util.EmojiUtil;
import devilsen.me.emojicreator.util.IntentUtil;
import devilsen.me.emojicreator.util.ShareUti;
import devilsen.me.emojicreator.util.analyze.Umeng;
import devilsen.me.emojicreator.widget.ListItemDialog;
import devilsen.me.emojicreator.widget.SpacesItemDecoration;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * author : dongSen
 * date : 2017-02-22 17:05
 * desc : 图片列表
 */
public class ImageListFragment extends BaseFragment implements ListContract.View, SourceListAdapter.ItemClickListener {

    private ListContract.Presenter mPresenter;

    private SourceListAdapter mAdapter;

    private SwipeRefreshLayout mEmojiRefreshLayout;
    private StaggeredGridLayoutManager mLayoutManager;
    private LinearLayout mEmptyLayout;

    private int type = Constant.TYPE_LUCK;
    private int page;
    private boolean canLoadMore;

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

        mEmojiRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.list_source_sr);
        RecyclerView emojiRecyclerView = (RecyclerView) view.findViewById(R.id.list_source_rv);
        mEmptyLayout = (LinearLayout) view.findViewById(R.id.layout_empty);

        mAdapter = new SourceListAdapter(this);
        emojiRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        emojiRecyclerView.setLayoutManager(mLayoutManager);
        emojiRecyclerView.addItemDecoration(new SpacesItemDecoration(12));
        mEmojiRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mEmojiRefreshLayout.setOnRefreshListener(() -> loadImage(true));
        mAdapter.setItemClickListener(this);

        mPresenter = new ListPresenter(Injection.provideEmojiRepository(getContext()),
                this,
                Injection.provideSchedulersProvider());

        emojiRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onScroll();
                }
            }
        });

        if (type == Constant.TYPE_LUCK) {
            loadImage(true);
            canLoadMore = false;
            firstVisible = false;

            FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_fresh);
            fab.setOnClickListener(v -> loadImage(true));
        } else if (type == Constant.TYPE_LOCAL) {
            canLoadMore = false;
        } else {
            canLoadMore = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && firstVisible) {
            loadImage(true);
            firstVisible = false;
        }
    }

    private void onScroll() {
        if (!canLoadMore)
            return;

        int[] positions = new int[mLayoutManager.getSpanCount()];
        mLayoutManager.findLastVisibleItemPositions(positions);

        for (int position : positions) {
            if (position == mLayoutManager.getItemCount() - 1) {
                page++;
                loadImage(false);
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
        Umeng.fragmentResume(Constant.getNameByType(type));
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unSubscribe();
        Umeng.fragmentPause(Constant.getNameByType(type));
    }

    @Override
    public void showEmpty(boolean isEmpty) {
        if (isEmpty) {
            mEmojiRefreshLayout.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            mEmojiRefreshLayout.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmojis(List<ImageBean> listData) {
        if (page == 0) {
            mAdapter.replaceData(listData);
        } else {
            mAdapter.addData(listData);
        }

        showEmpty(mAdapter.getListSize() == 0);

        mEmojiRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadImage(boolean isFresh) {
        if (isFresh) {
            if (mEmojiRefreshLayout != null)
                mEmojiRefreshLayout.setRefreshing(true);
            page = 0;
        }

        if (mPresenter != null)
            mPresenter.loadList(type, page);
    }

    @Override
    public void stopFresh() {
        mEmojiRefreshLayout.setRefreshing(false);
    }

    @Override
    public void createEmoji(ImageBean imageBean) {

    }

    @Override
    public void showLongClickDialog(ImageBean bean, int position) {
        if (type != Constant.TYPE_LOCAL)
            return;

        new ListItemDialog
                .Builder(getContext())
                .setOnDialogClickListener(v -> Observable.just(EmojiUtil.getInstance().deleteEmoji(mContext, bean))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                //next
                                aBoolean -> {
                                },
                                //error
                                e -> Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show(),
                                //complete
                                () -> mAdapter.deleteItem(position)
                        ))
                .create()
                .show();
    }

    @Override
    public void setPresenter(ListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onItemClick(@NonNull ImageBean bean, View imageView) {
        if (type != Constant.TYPE_LOCAL) {
            IntentUtil.startImg(mActivity, bean, imageView);
        } else {
            ShareUti.shareImg(mActivity, bean.path, "图片");
        }
    }

    @Override
    public void onItemLongClick(@NonNull ImageBean bean, int position) {
        showLongClickDialog(bean, position);
    }
}
