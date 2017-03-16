package devilsen.me.emojicreator.task;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * handler error
 */
class ErrorCallAdapterFactory extends CallAdapter.Factory {

    public static ErrorCallAdapterFactory create() {
        return new ErrorCallAdapterFactory();
    }

    private ErrorCallAdapterFactory() {
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return null;
    }
}
