package devilsen.me.emojicreator.net;

import okhttp3.OkHttpClient;

/**
 * author : dongSen
 * date : 2016-06-03 10:47
 * desc : OkHttpClient
 */
public class OkHttpInstance {

    private static OkHttpClient.Builder httpClient;

    public static OkHttpClient.Builder getInstance() {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new LoggingInterceptor());
        }
        return httpClient;
    }

}
