package devilsen.me.emojicreator.net;


import java.io.IOException;

import devilsen.me.emojicreator.util.Log4Utils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author : dongSen
 * date : 2016-06-03 11:19
 * desc : log拦截器
 */
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) {
        Request request = chain.request();

        long t1 = System.nanoTime();

        Log4Utils.i("http request", String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long t2 = System.nanoTime();

        if (response != null && response.headers() != null)
            Log4Utils.i("http response", String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }
}
