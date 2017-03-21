package devilsen.me.emojicreator.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import devilsen.me.emojicreator.Constant;
import devilsen.me.emojicreator.data.ImageBean;
import devilsen.me.emojicreator.data.source.EmojiDataSource;
import devilsen.me.emojicreator.net.ApiService;
import devilsen.me.emojicreator.util.schedulers.BaseSchedulersProvider;
import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;
import static devilsen.me.emojicreator.Constant.OFF_SET;

/**
 * author : dongSen
 * date : 2017-02-23 16:21
 * desc :
 */
public class EmojisRemoteDataSource implements EmojiDataSource {

    @Nullable
    private static EmojisRemoteDataSource INSTANCE;

    @NonNull
    private BaseSchedulersProvider mSchedulersProvider;

    private EmojisRemoteDataSource(@NonNull BaseSchedulersProvider schedulersProvider) {
        mSchedulersProvider = checkNotNull(schedulersProvider);
    }

    public static EmojisRemoteDataSource getInstance(@NonNull BaseSchedulersProvider schedulersProvider) {
        if (INSTANCE == null)
            INSTANCE = new EmojisRemoteDataSource(schedulersProvider);
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * @param type {@link Constant}
     * @param page index page
     * @return an observable list
     */
    @Override
    public Observable<List<ImageBean>> getList(int type, int page) {
        Observable<List<ImageBean>> observable;
        switch (type) {
            case Constant.TYPE_LUCK:
                observable = ApiService.getUrlApi().getGoodLuckList(page * OFF_SET, OFF_SET);
                break;
            case Constant.TYPE_HOT:
                observable = ApiService.getUrlApi().getHotList(page * OFF_SET, OFF_SET);
                break;
            case Constant.TYPE_ALL:
                observable = ApiService.getUrlApi().getList(page * OFF_SET, OFF_SET);
                break;
            default:
                observable = ApiService.getUrlApi().getRecommendList(page * OFF_SET, OFF_SET);
                break;
        }
        return observable;
    }


}
