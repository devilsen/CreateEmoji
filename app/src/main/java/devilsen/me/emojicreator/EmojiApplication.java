package devilsen.me.emojicreator;


import android.app.Application;

/**
 * author : dongSen
 * date : 2017-02-22 16:49
 * desc :
 */
public class EmojiApplication extends Application {

    private static EmojiApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
//
//        //使用集成测试服务
//        MobclickAgent.setDebugMode(true);

        INSTANCE = this;

    }

    public static EmojiApplication getInstance() {
        return INSTANCE;
    }
}
