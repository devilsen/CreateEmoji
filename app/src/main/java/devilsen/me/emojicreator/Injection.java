package devilsen.me.emojicreator;

import android.content.Context;
import android.support.annotation.NonNull;

import devilsen.me.emojicreator.data.source.EmojiDataSource;
import devilsen.me.emojicreator.data.source.EmojiRepository;
import devilsen.me.emojicreator.data.source.local.EmojisLocalDataSource;
import devilsen.me.emojicreator.data.source.remote.EmojisRemoteDataSource;
import devilsen.me.emojicreator.data.source.uploadlist.UploadListData;
import devilsen.me.emojicreator.data.source.uploadlist.UploadListDataSource;
import devilsen.me.emojicreator.util.schedulers.BaseSchedulersProvider;
import devilsen.me.emojicreator.util.schedulers.SchedulersProvider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link EmojiDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

//    public static EmojiDataSource provideEmojiRepository(@NonNull Context context) {
//        checkNotNull(context);
//        return EmojiRepository.getInstance(EmojisLocalDataSource.getInstance(provideSchedulersProvider()),
//                EmojisRemoteDataSource.getInstance(provideSchedulersProvider()));
//    }

    public static EmojiRepository provideEmojiRepository(@NonNull Context context) {
        checkNotNull(context);
        return EmojiRepository.getInstance(EmojisLocalDataSource.getInstance(context),
                EmojisRemoteDataSource.getInstance(provideSchedulersProvider()));
    }

    public static UploadListData provideUploadRepository(@NonNull Context context) {
        return UploadListDataSource.getInstance(context, provideSchedulersProvider());
    }

    public static BaseSchedulersProvider provideSchedulersProvider() {
        return SchedulersProvider.getInstance();
    }

}
