package devilsen.me.emojicreator.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import devilsen.me.emojicreator.data.ImageBean;
import devilsen.me.emojicreator.data.source.EmojiDataSource;
import devilsen.me.emojicreator.util.schedulers.BaseSchedulersProvider;
import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

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

    @Override
    public Observable<List<ImageBean>> getList(int page) {
        return null;
    }
}
