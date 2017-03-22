package devilsen.me.emojicreator.util;

import android.graphics.BitmapFactory;

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

    public ImageSize decodeImageSize(String imagePath) {
        BitmapFactory.decodeFile(imagePath, options);
        return new ImageSize(options.outWidth, options.outHeight);
    }

    public static class ImageSize{
        public int width;
        public int height;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }


}
