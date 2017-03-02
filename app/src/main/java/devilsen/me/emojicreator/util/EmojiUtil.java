package devilsen.me.emojicreator.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import devilsen.me.emojicreator.data.ImageBean;

/**
 * Description : 保存图片表情
 * author : dongsen
 * Time : 2016-02-27
 */
public class EmojiUtil {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());

    public static final String fileDir = "createEmoji";

    private static EmojiUtil instance;

    private File file;

    private File dir = new File(getSavePath());

    public static EmojiUtil getInstance() {
        if (instance == null) {
            instance = new EmojiUtil();
        }
        return instance;
    }

    /**
     * 保存表情
     *
     * @param context
     * @param tempBitmap
     */
    public boolean saveEmoji(Context context, String title, Bitmap tempBitmap) {
        String bitName = title + dateFormat.format(new Date());
        file = new File(dir, bitName + ".png");

        BufferedOutputStream fOut = null;
        try {
            if (!dir.exists()) {
                dir.mkdir();
            }
            fOut = new BufferedOutputStream(new FileOutputStream(file));

            tempBitmap.compress(Bitmap.CompressFormat.PNG, 50, fOut);

            fOut.flush();

            scanPhoto(context, file);

            return true;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        } finally {
            try {
                if (fOut != null)
                    fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取保存文件的绝对路径
     *
     * @return 路径
     */
    public String getFilePath() {
        if (file != null)
            return file.getAbsolutePath();

        return null;
    }

    public void resetFile() {
        file = null;
    }

    /**
     * 获取保存路径
     *
     * @return sd卡 or 手机
     */
    private String getSavePath() {
        String saveDir;
        boolean sdCardExit = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExit) {
            saveDir = Environment.getExternalStorageDirectory().getPath();
        } else {
            saveDir = Environment.getDataDirectory().getPath();
        }
        return saveDir + "/" + fileDir + "/";
    }

    /**
     * 向系统发送广播
     *
     * @param imgFile
     */
    private void scanPhoto(Context mContext, File imgFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imgFile);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }


    /**
     * 删除表情
     *
     * @param bean 本地表情
     */
    public boolean deleteEmoji(@NonNull Context context, @NonNull ImageBean bean) {
        String imgPath = bean.path;
        if (imgPath == null)
            return false;

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = MediaStore.Images.Media.query(resolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=?",
                new String[]{imgPath},
                null);
        boolean result = false;
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uri = ContentUris.withAppendedId(contentUri, id);
            resolver.delete(uri, null, null);

            File file = new File(imgPath);
            result = file.delete();
        }
        return result;
    }
}
