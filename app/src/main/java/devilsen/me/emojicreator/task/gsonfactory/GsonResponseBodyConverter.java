package devilsen.me.emojicreator.task.gsonfactory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * author : dongSen
 * date : 2017-02-27 15:28
 * desc :
 */
final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            String body = value.string();

            JSONObject jsonObject = new JSONObject(body);

            String status = jsonObject.optString("status");

            if ("success".equals(status)) {
                if (jsonObject.has("datas")) {
                    Object data = jsonObject.get("datas");
                    return adapter.fromJson(data.toString());
                } else {
                    return (T) status;
                }
            } else {
                throw new RuntimeException(status);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            value.close();
        }
    }
}