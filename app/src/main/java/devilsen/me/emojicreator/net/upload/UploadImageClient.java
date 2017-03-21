package devilsen.me.emojicreator.net.upload;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import devilsen.me.emojicreator.EmojiApplication;
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
    public void upload(String name, @NonNull String imagePath) {
        ApiService.getUrlApi().uploadImage(
                RequestBody.create(MEDIA_TYPE_TXT, name)
                , RequestBody.create(MEDIA_TYPE_PNG, new File(imagePath)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(EmojiApplication.getInstance().getApplicationContext(), "分享成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error", e.toString());
                    }

                    @Override
                    public void onNext(String s) {
                    }
                });

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
