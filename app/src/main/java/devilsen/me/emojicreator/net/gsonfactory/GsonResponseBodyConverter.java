package devilsen.me.emojicreator.net.gsonfactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;

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
    private final Gson gson;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.adapter = adapter;
        this.gson = gson;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
//        JsonReader jsonReader = gson.newJsonReader(value.charStream());
//        try {
//            jsonReader.beginObject();
//            while (jsonReader.hasNext()){
//                switch (jsonReader.nextName()){
//                    case "status":
//                        String status = jsonReader.nextString();
//                        break;
//                    case "datas":
//                        jsonReader.
//                        break;
//                }
//            }
//
//
//
//            return adapter.read(jsonReader);
//        } finally {
//            value.close();
//        }

        try {
            String body = value.string();

            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(body).getAsJsonObject();
            JsonElement status = jsonObject.get("status");

            if ("success".equals(status.getAsString())) {
                JsonElement datas = jsonObject.get("datas");
                return adapter.fromJsonTree(datas);
            } else {
                throw new RuntimeException(status.getAsString());
            }
        } finally {
            value.close();
        }
    }
}