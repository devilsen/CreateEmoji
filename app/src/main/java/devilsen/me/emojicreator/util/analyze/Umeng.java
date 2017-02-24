package devilsen.me.emojicreator.util.analyze;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * author : dongSen
 * date : 2016-08-04 17:47
 * desc : 友盟分析
 */
public class Umeng {


    /**
     * author : dongSen
     * desc : 只有activity resume
     */
    public static void activityResume(Context context, String name) {
        MobclickAgent.onPageStart(name); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(context);          //统计时长
    }

    /**
     * author : dongSen
     * desc : 只有activity pause
     */
    public static void activityPause(Context context, String name) {
        MobclickAgent.onPageEnd(name); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(context);
    }

    /**
     * author : dongSen
     * desc : 包含fragment的activity中
     */
    public static void fragmentActivityResume(Context context) {
        MobclickAgent.onResume(context);       //统计时长
    }

    /**
     * fragment 中
     *
     * @param name fragment name
     */
    public static void fragmentResume(String name) {
        MobclickAgent.onPageStart(name); //统计页面，"MainScreen"为页面名称，可自定义
    }

    /**
     * author : dongSen
     * desc : 包含fragment的activity中
     */
    public static void fragmentActivityPause(Context context) {
        MobclickAgent.onPause(context);
    }

    /**
     * fragment 中
     *
     * @param name fragment name
     */
    public static void fragmentPause(String name) {
        MobclickAgent.onPageEnd(name);
    }

    /**
     * 记录点击事件
     *
     * @param eventId event id
     */
    public static void clickEvent(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
    }


    public static class EventId{
        //完成按钮
        public static final String DOWN = "done";
        //重做按钮
        public static final String REDO = "redo";
        //保存按钮
        public static final String SAVE = "save";
        //分享按钮
        public static final String SHARE = "share";

    }
}
