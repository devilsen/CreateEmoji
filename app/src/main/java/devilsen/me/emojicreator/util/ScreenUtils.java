package devilsen.me.emojicreator.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * author : dongSen
 * date : 2017-03-01 17:37
 * desc :
 */
public class ScreenUtils {

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

}
