package devilsen.me.emojicreator.data.db;


import android.provider.BaseColumns;

/**
 * author : dongSen
 * date : 2017/3/17 下午6:02
 * desc : 所需数据库字段
 */
public class EmojiPersistenceContract {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "emoji.db";

    private EmojiPersistenceContract() {
    }

    //上传图片实例
    public static abstract class UploadEntry implements BaseColumns {
        public static final String TABLE_NAME = "upload";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_IMAGE_NAME = "name";
        public static final String COLUMN_NAME_IMAGE_PATH = "path";
        public static final String COLUMN_NAME_UPLOAD_STATUS = "status";//上传是否成功
    }

}
