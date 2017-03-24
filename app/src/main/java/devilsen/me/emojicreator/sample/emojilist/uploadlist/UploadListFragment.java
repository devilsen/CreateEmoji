package devilsen.me.emojicreator.sample.emojilist.uploadlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import devilsen.me.emojicreator.Injection;
import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.data.source.uploadlist.UploadImageBean;
import devilsen.me.emojicreator.sample.BaseFragment;
import devilsen.me.emojicreator.sample.uploadimage.UploadListAdapter;
import devilsen.me.emojicreator.widget.SpacesItemDecoration;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * author : dongSen
 * date : 2017/3/20 下午5:58
 * desc : 上传图片列表
 */
public class UploadListFragment extends BaseFragment implements UploadListContract.View, UploadListAdapter.ItemClickListener {

    private SwipeRefreshLayout mEmojiRefreshLayout;
    private StaggeredGridLayoutManager mLayoutManager;
    private LinearLayout mEmptyLayout;

    private UploadListContract.Presenter mPresenter;
    private boolean firstVisible = true;
    private UploadListAdapter mAdapter;

    public static Fragment newInstance() {
        return new UploadListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_good_luck, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEmojiRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.list_source_sr);
        RecyclerView emojiRecyclerView = (RecyclerView) view.findViewById(R.id.list_source_rv);
        mEmptyLayout = (LinearLayout) view.findViewById(R.id.layout_empty);
//        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.goodluck_fab);

        mAdapter = new UploadListAdapter(this);
        emojiRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        emojiRecyclerView.setLayoutManager(mLayoutManager);
        emojiRecyclerView.addItemDecoration(new SpacesItemDecoration(12));
        mEmojiRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mEmojiRefreshLayout.setOnRefreshListener(this::loadImages);
//        fab.setOnClickListener(v -> loadImages());
        mAdapter.setItemClickListener(this);

        mPresenter = new UploadListPresenter(
                Injection.provideUploadRepository(getContext()),
                this,
                Injection.provideSchedulersProvider());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && firstVisible) {
            loadImages();
            firstVisible = false;
        }
    }

    @Override
    public void loadImages() {
        mPresenter.loadImages();
    }

    @Override
    public void showImages(List<UploadImageBean> imageList) {
        mAdapter.replaceData(imageList);
        showEmpty(imageList.isEmpty());
        stopFresh();
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
    public void stopFresh() {
        mEmojiRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setPresenter(UploadListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onItemClick(UploadImageBean bean, View imageView) {

    }

    @Override
    public void onItemLongClick(UploadImageBean bean, int position) {

    }
}
