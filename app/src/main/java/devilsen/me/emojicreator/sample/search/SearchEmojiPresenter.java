package devilsen.me.emojicreator.sample.search;

import android.support.annotation.NonNull;

import devilsen.me.emojicreator.data.source.EmojiRepository;
import devilsen.me.emojicreator.util.schedulers.BaseSchedulersProvider;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * author : dongSen
 * date : 2017-03-02 16:46
 * desc :
 */
public class SearchEmojiPresenter implements SearchEmojiContract.Presenter {

    @NonNull
    private EmojiRepository mEmojiRepository;

    @NonNull
    private SearchEmojiContract.View mSearchView;

    @NonNull
    private BaseSchedulersProvider mSchedulersProvider;

    @NonNull
    private CompositeSubscription mSubscription;

    public SearchEmojiPresenter(@NonNull EmojiRepository mEmojiRepository,
                                @NonNull SearchEmojiContract.View mSearchView,
                                @NonNull BaseSchedulersProvider mSchedulersProvider) {
        this.mEmojiRepository = checkNotNull(mEmojiRepository, "repository cannot be null");
        this.mSearchView = checkNotNull(mSearchView, "view cannot be null");
        this.mSchedulersProvider = checkNotNull(mSchedulersProvider, "scheduler cannot be null");

        mSubscription = new CompositeSubscription();
    }

    @Override
    public void loadEmojis(String keyword, int page) {
        mSubscription.clear();

        Subscription subscribe = mEmojiRepository.getSearchList(keyword, page)
                .subscribeOn(mSchedulersProvider.io())
                .observeOn(mSchedulersProvider.ui())
                .subscribe(
                        //next
                        mSearchView::showEmojis,
                        //error
                        e -> {
                            e.printStackTrace();
                            mSearchView.stopFresh();
                        }
                );

        mSubscription.add(subscribe);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
