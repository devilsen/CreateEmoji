package devilsen.me.emojicreator.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * author : dongSen
 * date : 2016-05-27 16:51
 * desc : 分享图片
 */
public class ShareUti {

    /**
     * 分享图片
     *
     * @param context
     * @param uri     Uri uri = Uri.fromFile(new File("path"));
     * @param title
     */
    public static void shareImg(Context context, Uri uri, String title) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(intent, title));
    }


    /**
     * 分享图片
     *
     * @param context
     * @param path
     * @param title
     */
    public static void shareImg(Context context, String path, String title) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        intent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(intent, title));
    }


}
