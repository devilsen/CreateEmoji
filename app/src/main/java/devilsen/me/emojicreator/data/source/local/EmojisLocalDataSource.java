package devilsen.me.emojicreator.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pushtorefresh.storio.contentresolver.impl.DefaultStorIOContentResolver;
import com.pushtorefresh.storio.contentresolver.operations.get.DefaultGetResolver;
import com.pushtorefresh.storio.contentresolver.queries.Query;

import java.util.ArrayList;
import java.util.List;

import devilsen.me.emojicreator.data.ImageBean;
import devilsen.me.emojicreator.data.source.EmojiDataSource;
import devilsen.me.emojicreator.data.source.local.entity.Photo;
import rx.Observable;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

/**
 * author : dongSen
 * date : 2017-02-23 15:47
 * desc : get emojis from local disk
 */
public class EmojisLocalDataSource implements EmojiDataSource {

    @Nullable
    private static EmojisLocalDataSource INSTANCE;

    @NonNull
    private DefaultStorIOContentResolver resolver;

    private EmojisLocalDataSource(@NonNull Context context) {
        resolver = DefaultStorIOContentResolver.builder()
                .contentResolver(context.getContentResolver())
                .build();
    }

    public static EmojisLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new EmojisLocalDataSource(context);
        }
        return INSTANCE;
    }


    @Override
    public Observable<List<ImageBean>> getList(int page) {
        return getListFromLocal();
    }

    private Observable<List<ImageBean>> getListFromLocal() {
        return resolver.get()
                .listOfObjects(Photo.class)
                .withQuery(Query.builder()
                        .uri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        .columns(MediaStore.Images.Media._ID,
                                MediaStore.Images.Media.DATA,
                                MediaStore.Images.Media.BUCKET_ID,
                                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                                MediaStore.Images.Media.DATE_ADDED)
                        .where(MIME_TYPE + "=? or " + MIME_TYPE + "=? " + "or " + MIME_TYPE + "=?")
                        .whereArgs("image/jpeg", "image/png", "image/gif")
                        .sortOrder(MediaStore.Images.Media.DATE_ADDED + " DESC")
                        .build())
                .withGetResolver(new DefaultGetResolver<Photo>() {
                    @NonNull
                    @Override
                    public Photo mapFromCursor(@NonNull Cursor cursor) {
                        return Photo.from(cursor);
                    }
                })
                .prepare()
                .asRxObservable()
                .map(this::toImageBean);
    }

    private List<ImageBean> toImageBean(List<Photo> photos) {
        List<ImageBean> list = new ArrayList<>(photos.size());
        for (int i = 0; i < photos.size(); i++) {
            Photo photo = photos.get(i);
            if ("createEmoji".equals(photo.getDirectory())) {
                ImageBean imageBean = new ImageBean();
                imageBean._id = String.valueOf(photo.getId());
                imageBean.path = photo.getPath();
                list.add(imageBean);
            }
        }
        return list;
    }


//    private Observable<List<ImageBean>> getListFromLocal() {
//        return resolver.get()
//                .object(Photo.class)
//                .withQuery(Query.builder()
//                        .uri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                        .columns(MediaStore.Images.Media._ID,
//                                MediaStore.Images.Media.DATA,
//                                MediaStore.Images.Media.BUCKET_ID,
//                                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
//                                MediaStore.Images.Media.DATE_ADDED)
//                        .where(MIME_TYPE + "=? or " + MIME_TYPE + "=? " + "or " + MIME_TYPE + "=?")
//                        .whereArgs("image/jpeg", "image/png", "image/gif")
//                        .sortOrder(MediaStore.Images.Media.DATE_ADDED + " DESC")
//                        .build())
//                .withGetResolver(new DefaultGetResolver<Photo>() {
//                    @NonNull
//                    @Override
//                    public Photo mapFromCursor(@NonNull Cursor cursor) {
//                        return Photo.from(cursor);
//                    }
//                })
//                .prepare()
//                .asRxObservable()
//                .filter(photo -> "createEmoji".equals(photo.getDirectory()))
//                .map(photo -> new ImageBean(String.valueOf(photo.getId()), photo.getPath()))
//                .toList();
//    }
}
