package devilsen.me.emojicreator.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * author : dongSen
 * date : 2017/3/23 下午5:10
 * desc : 图片工具类
 */
public class ImageUtil {


    public static Bitmap getBitmap(@NonNull String filePath, int maxWidth, int maxHeight) {
        if (filePath.trim().length() == 0) {
            return null;
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeFile(filePath, options);
            short angle = 0;

            ExifInterface exif = null;
            if (filePath.endsWith("jpeg")) {
                try {
                    exif = new ExifInterface(filePath);
                } catch (IOException var8) {
                    var8.printStackTrace();
                    exif = null;
                }
            }

            if (exif != null) {
                int m = exif.getAttributeInt("Orientation", 0);
                switch (m) {
                    case 3:
                        angle = 180;
                        break;
                    case 6:
                        angle = 90;
                        break;
                    case 8:
                        angle = 270;
                        break;
                    default:
                        angle = 0;
                }
            }

            if (angle != 0) {
                Matrix m1 = new Matrix();
                m1.postRotate((float) angle);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m1, true);
            }

            return bm;
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        if (maxWidth != 0 && maxHeight != 0) {
            int height = options.outHeight;
            int width = options.outWidth;

            int inSampleSize;
            for (inSampleSize = 1; (height >>= 1) >= maxHeight && (width >>= 1) >= maxWidth; inSampleSize <<= 1) {
                ;
            }

            return inSampleSize;
        } else {
            return 1;
        }
    }

    private static final long twoMSize = 2000000L;

    /**
     * 压缩图片
     *
     * @param imagePath 原图片路径
     *                  1.对gif不进行处理
     *                  2.对 < 2M的图片不处理
     * @return 压缩路径
     */
    public static File compressionImage(String imagePath) {
        if (imagePath.endsWith("gif")) {
            return new File(imagePath);
        }

        File sourceFile = new File(imagePath);

        if (sourceFile.length() < twoMSize) {
            return sourceFile;
        }

        Bitmap bitmap = ImageUtil.getBitmap(imagePath, 200, 200);

        String thumbPath = EmojiUtil.getInstance().getCacheDir() + System.currentTimeMillis() + ".png";
        File file = new File(thumbPath);
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 80, out)) {
                out.flush();
                out.close();
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
