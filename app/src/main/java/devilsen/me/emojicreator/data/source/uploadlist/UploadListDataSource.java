package devilsen.me.emojicreator.data.source.uploadlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import devilsen.me.emojicreator.data.db.upload.UploadDbHelper;
import devilsen.me.emojicreator.util.schedulers.BaseSchedulersProvider;
import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;
import static devilsen.me.emojicreator.data.db.EmojiPersistenceContract.UploadEntry;

/**
 * 上传列表数据
 */
public class UploadListDataSource implements UploadListData {

    @Nullable
    private static UploadListDataSource INSTANCE;

    @NonNull
    private BriteDatabase mBriteDatabase;

    private UploadListDataSource(@NonNull Context context,
                                 @NonNull BaseSchedulersProvider schedulersProvider) {
        checkNotNull(context, "the context can't be null");
        checkNotNull(schedulersProvider, "the schedulersProvider can't be null");
        UploadDbHelper dbHelper = new UploadDbHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        mBriteDatabase = sqlBrite.wrapDatabaseHelper(dbHelper, schedulersProvider.io());
    }

    public static UploadListDataSource getInstance(
            @NonNull Context context,
            @NonNull BaseSchedulersProvider schedulersProvider) {
        if (INSTANCE == null) {
            INSTANCE = new UploadListDataSource(context, schedulersProvider);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<UploadImageBean>> getUploadList() {
        String[] projection = {
                UploadEntry.COLUMN_NAME_ENTRY_ID,
                UploadEntry.COLUMN_NAME_IMAGE_NAME,
                UploadEntry.COLUMN_NAME_IMAGE_PATH,
                UploadEntry.COLUMN_NAME_UPLOAD_STATUS
        };
        String sql = String.format("SELECT %s FROM %s DESC", TextUtils.join(",", projection), UploadEntry.TABLE_NAME);
        return mBriteDatabase.createQuery(UploadEntry.TABLE_NAME, sql)
                .mapToList(this::getUploadImage);
    }

    private UploadImageBean getUploadImage(@NonNull Cursor c) {
        String id = c.getString(c.getColumnIndexOrThrow(UploadEntry.COLUMN_NAME_ENTRY_ID));
        String name = c.getString(c.getColumnIndexOrThrow(UploadEntry.COLUMN_NAME_IMAGE_NAME));
        String path = c.getString(c.getColumnIndexOrThrow(UploadEntry.COLUMN_NAME_IMAGE_PATH));
        boolean status = c.getInt(c.getColumnIndexOrThrow(UploadEntry.COLUMN_NAME_UPLOAD_STATUS)) == 1;

        return new UploadImageBean(id, name, path, status);
    }

    @Override
    public void uploadImage(@NonNull UploadImageBean imageBean) {
        checkNotNull(imageBean);
        ContentValues values = new ContentValues();
        values.put(UploadEntry.COLUMN_NAME_ENTRY_ID, imageBean.id);
        values.put(UploadEntry.COLUMN_NAME_IMAGE_NAME, imageBean.name);
        values.put(UploadEntry.COLUMN_NAME_IMAGE_PATH, imageBean.path);
        values.put(UploadEntry.COLUMN_NAME_UPLOAD_STATUS, imageBean.status);
        mBriteDatabase.insert(UploadEntry.TABLE_NAME, values);
    }

    @Override
    public void deleteImage(@NonNull UploadImageBean imageBean) {
        checkNotNull(imageBean);
        String selection = UploadEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        mBriteDatabase.delete(UploadEntry.TABLE_NAME, selection, imageBean.id);
    }

    @Override
    public void updateImage(@NonNull UploadImageBean imageBean) {
        checkNotNull(imageBean);
        ContentValues contentValues = new ContentValues();
        contentValues.put(UploadEntry.COLUMN_NAME_UPLOAD_STATUS, imageBean.status);

        String selection = UploadEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        mBriteDatabase.update(UploadEntry.TABLE_NAME, contentValues, selection, imageBean.id);
    }


}
