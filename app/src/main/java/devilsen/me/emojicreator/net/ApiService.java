package devilsen.me.emojicreator.net;

import java.util.concurrent.TimeUnit;

import devilsen.me.emojicreator.net.gsonfactory.CustomerConverterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Description :  retrofit
 * author : dongsen
 * Time : 2016-03-01
 */
public class ApiService {

    public static final String HOST = "https://backblog.me";

    private static final String BASE_URL = HOST + "/api/";

    private static UrlApi instance;

    private static UploadApi uploadApi;

    private static Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(CustomerConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(OkHttpInstance.getInstance().build())
            .build();


    private static Retrofit mUploadRetrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(CustomerConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(40, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build())
            .build();

    /**
     * 获取接口信息
     *
     * @return 接口列表
     */
    public static UrlApi getUrlApi() {
        if (instance == null)
            synchronized (ApiService.class) {
                if (instance == null) {
                    instance = mRetrofit.create(UrlApi.class);
                }
            }
        return instance;
    }

    /**
     * 获取上传图片接口信息
     *
     * @return 上传图片接口列表
     */
    public static UploadApi getUploadApi() {
        if (uploadApi == null) {
            synchronized (ApiService.class) {
                if (uploadApi == null) {
                    uploadApi = mUploadRetrofit.create(UploadApi.class);
                }
            }
        }
        return uploadApi;
    }
}
