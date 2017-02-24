package devilsen.me.emojicreator.sample.emojilist.imagelist;

import android.support.annotation.NonNull;
import android.util.Log;

import devilsen.me.emojicreator.data.source.EmojiRepository;
import devilsen.me.emojicreator.util.schedulers.BaseSchedulersProvider;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * author : dongSen
 * date : 2017-02-22 17:39
 * desc :
 */
public class ListPresenter implements ListContract.Presenter {

    @NonNull
    private final EmojiRepository mEmojiRepository;

    @NonNull
    private final ListContract.View mEmojiView;

    @NonNull
    private final BaseSchedulersProvider mSchedulerProvider;

    @NonNull
    private CompositeSubscription mSubscription;

    public ListPresenter(@NonNull EmojiRepository mEmojiRepository,
                         @NonNull ListContract.View mEmojiView,
                         @NonNull BaseSchedulersProvider mSchedulerProvider) {
        this.mEmojiRepository = checkNotNull(mEmojiRepository, "repository cannot be null");
        this.mEmojiView = checkNotNull(mEmojiView, "view cannot be null");
        this.mSchedulerProvider = checkNotNull(mSchedulerProvider, "scheduler cannot be null");

        mSubscription = new CompositeSubscription();
        mEmojiView.setPresenter(this);
    }

    @Override
    public void loadList(int type, int page) {
        mSubscription.clear();

        Subscription subscribe = mEmojiRepository.getList(page)
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(
                        //onNext
                        list -> {
                            mEmojiView.showEmojis(list);//list -> mEmojiView.showEmojis(list),
                            Log.e("list.size", list.size() + "");
                        },
                        //onError
                        Throwable::printStackTrace,//e -> e.printStackTrace(),
                        //onComplete
                        () -> Log.e("complete", "complete")
                );
        mSubscription.add(subscribe);
    }

    @Override
    public void subscribe() {
        loadList(0, 0);
    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }
}
