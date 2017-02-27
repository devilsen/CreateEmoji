package devilsen.me.emojicreator.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import devilsen.me.emojicreator.Constant;
import devilsen.me.emojicreator.data.ImageBean;
import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * author : dongSen
 * date : 2017-02-22 15:40
 * desc : Concrete implementation to load tasks from the data sources into a cache.
 */
public class EmojiRepository implements EmojiDataSource {

    @Nullable
    private static EmojiRepository INSTANCE = null;

    @NonNull
    private final EmojiDataSource mLocalDataSource;

    @NonNull
    private final EmojiDataSource mRemoteDataSource;


    private EmojiRepository(@NonNull EmojiDataSource mLocalDataSource,
                            @NonNull EmojiDataSource mRemoteDataSource) {
        this.mLocalDataSource = checkNotNull(mLocalDataSource);
        this.mRemoteDataSource = checkNotNull(mRemoteDataSource);
    }

    public static EmojiRepository getInstance(@NonNull EmojiDataSource mLocalDataSource,
                                              @NonNull EmojiDataSource mRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new EmojiRepository(mLocalDataSource, mRemoteDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(EmojiDataSource, EmojiDataSource)} to create a new instance
     * next time it's called.`
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public Observable<List<ImageBean>> getList(int type, int page) {
        if (type == Constant.TYPE_LOCAL) {
            return getLocalEmojis();
        } else {
            return getRemoteEmojis(type, page);
        }
    }

    private Observable<List<ImageBean>> getLocalEmojis() {
        return mLocalDataSource.getList(0, 0);//默认全部
    }

    private Observable<List<ImageBean>> getRemoteEmojis(int type, int page) {
        return mRemoteDataSource.getList(type, page);
    }
}
