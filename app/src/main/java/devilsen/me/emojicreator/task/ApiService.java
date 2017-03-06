package devilsen.me.emojicreator.task;

import devilsen.me.emojicreator.task.gsonfactory.CustomerConverterFactory;
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

    private static Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(CustomerConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(OkHttpInstance.getInstance().build())
            .build();

    public static UrlApi getUrlApi() {
        if (instance == null)
            synchronized (ApiService.class) {
                if (instance == null) {
                    instance = mRetrofit.create(UrlApi.class);
                }
            }
        return instance;
    }
}
