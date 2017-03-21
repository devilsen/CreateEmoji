package devilsen.me.emojicreator;


import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;

import devilsen.me.emojicreator.net.imagepipeline.OkHttpImagePipelineConfigFactory;
import okhttp3.OkHttpClient;

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, new OkHttpClient.Builder().build())
                .build();
        Fresco.initialize(this, config);

        //使用集成测试服务
        MobclickAgent.setDebugMode(true);

        INSTANCE = this;

    }

    public static EmojiApplication getInstance() {
        return INSTANCE;
    }
}
