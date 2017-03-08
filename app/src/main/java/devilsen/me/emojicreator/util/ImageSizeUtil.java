package devilsen.me.emojicreator.util;

import android.graphics.BitmapFactory;

import devilsen.me.emojicreator.data.ImageBean;

/**
 * author : dongSen
 * date : 2017-02-28 11:29
 * desc :
 */
public class ImageSizeUtil {

    private static ImageSizeUtil INSTANCE;

    private BitmapFactory.Options options;

    private ImageSizeUtil() {
        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
    }

    public static ImageSizeUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImageSizeUtil();
        }
        return INSTANCE;
    }

    public void decodeImageAndSetSize(String imagePath, ImageBean bean) {
        BitmapFactory.decodeFile(imagePath, options);
        bean.size = new ImageBean.SizeBean(options.outWidth, options.outHeight);
    }


}
