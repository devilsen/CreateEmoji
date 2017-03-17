package devilsen.me.emojicreator.data.db.upload;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import devilsen.me.emojicreator.data.db.EmojiPersistenceContract;

/**
 * author : dongSen
 * date : 2017/3/17 下午5:59
 * desc : 上传图片数据库
 */
public class UploadDbHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + EmojiPersistenceContract.UploadEntry.TABLE_NAME + "(" +
                    EmojiPersistenceContract.UploadEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    EmojiPersistenceContract.UploadEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    EmojiPersistenceContract.UploadEntry.COLUMN_NAME_IMAGE_NAME + TEXT_TYPE + COMMA_SEP +
                    EmojiPersistenceContract.UploadEntry.COLUMN_NAME_IMAGE_PATH + TEXT_TYPE + COMMA_SEP +
                    EmojiPersistenceContract.UploadEntry.COLUMN_NAME_UPLOAD_STATUS + BOOLEAN_TYPE +
                    " )";

    public UploadDbHelper(Context context) {
        super(context, EmojiPersistenceContract.DATABASE_NAME, null, EmojiPersistenceContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //升级指令
    }
}
