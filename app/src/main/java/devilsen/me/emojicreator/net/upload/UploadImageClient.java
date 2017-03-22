package devilsen.me.emojicreator.net.upload;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

import devilsen.me.emojicreator.EmojiApplication;
import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.net.ApiService;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author : dongSen
 * date : 2017/3/21 下午4:09
 * desc : 上传图片组件
 */
public class UploadImageClient {

    //参数类型
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_TXT = MediaType.parse("text");

    private static UploadImageClient INSTANCE;

    private UploadImageClient() {
    }

    public static UploadImageClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UploadImageClient();
        }
        return INSTANCE;
    }

    /**
     * 上传图片
     *
     * @param name      图片名称
     * @param imagePath 图片路径
     */
    public void upload(Context context, String name, @NonNull String imagePath) {
        ApiService.getUploadApi().uploadImage(
                RequestBody.create(MEDIA_TYPE_TXT, name)
                , RequestBody.create(MEDIA_TYPE_PNG, new File(imagePath)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showNotification(context);
                    }

                    @Override
                    public void onNext(String s) {
                        Toast.makeText(EmojiApplication.getInstance().getApplicationContext(), "分享成功", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void showNotification(Context context) {

        Intent intent = new Intent();

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.upload_image_file_title))
                .setContentText(context.getString(R.string.upload_image_file_content))
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(10, notification);

    }

//    private void compressionImage() {
//        Bitmap bitmap = ImageUtils.getBitmap(path, 200, 200);
//
//        String thumbPath = Content.thumbPath + System.currentTimeMillis() + ".png";
//        File file = new File(thumbPath);
//        try {
//            if (!file.getParentFile().exists()) {
//                file.getParentFile().mkdirs();
//            }
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//
//            FileOutputStream out = new FileOutputStream(file);
//            if (bitmap.compress(Bitmap.CompressFormat.PNG, 80, out)) {
//                out.flush();
//                out.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
