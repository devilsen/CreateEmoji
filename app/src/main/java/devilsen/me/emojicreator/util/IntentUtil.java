package devilsen.me.emojicreator.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import devilsen.me.emojicreator.data.ImageBean;
import devilsen.me.emojicreator.sample.createemoji.CreateActivity;


/**
 * author : dongSen
 * date : 2016-06-03 16:28
 * desc :
 */
public class IntentUtil {

    public static final String ELEMENT_NAME = "img";

    public static void startImg(Activity activity, ImageBean imageBean, View imageView) {
        Intent intent = new Intent(activity, CreateActivity.class);
        intent.putExtra("name", imageBean.name);
        intent.putExtra("path", imageBean.path);
        intent.putExtra("width", imageBean.size.width);
        intent.putExtra("height", imageBean.size.height);

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imageView, ELEMENT_NAME);
        try {
            ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
        } catch (IllegalArgumentException e) {
            activity.startActivity(intent);
        }
    }


}
