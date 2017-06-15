package devilsen.me.emojicreator.net;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
            getUnsafeOkHttpClient(httpClient);
        }
        return httpClient;
    }


    private static void getUnsafeOkHttpClient(OkHttpClient.Builder builder) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier((hostname, session) -> true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
