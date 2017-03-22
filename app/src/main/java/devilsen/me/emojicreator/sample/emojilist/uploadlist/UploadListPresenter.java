package devilsen.me.emojicreator.sample.emojilist.uploadlist;

import android.support.annotation.NonNull;

import java.util.List;

import devilsen.me.emojicreator.data.source.uploadlist.UploadImageBean;
import devilsen.me.emojicreator.data.source.uploadlist.UploadListData;
import devilsen.me.emojicreator.util.schedulers.BaseSchedulersProvider;
import rx.Subscriber;

/**
 * author : dongSen
 * date : 2017/3/22 上午11:26
 * desc : 上传列表presenter
 */
public class UploadListPresenter implements UploadListContract.Presenter {

    private UploadListData mListData;

    private UploadListContract.View mView;

    private BaseSchedulersProvider mSchedulersProvider;

    public UploadListPresenter(@NonNull UploadListData listData,
                               @NonNull UploadListContract.View view,
                               @NonNull BaseSchedulersProvider schedulersProvider) {
        mListData = listData;
        mView = view;
        mSchedulersProvider = schedulersProvider;

        mView.setPresenter(this);

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void loadImages() {
        mListData.getUploadList()
        .observeOn(mSchedulersProvider.ui())
        .subscribeOn(mSchedulersProvider.io())
        .subscribe(
                new Subscriber<List<UploadImageBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.stopFresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.stopFresh();
                    }

                    @Override
                    public void onNext(List<UploadImageBean> imageList) {
                        mView.showImages(imageList);
                    }
                }
        );
    }
}
