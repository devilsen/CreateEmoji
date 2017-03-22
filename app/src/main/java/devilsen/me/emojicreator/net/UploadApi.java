package devilsen.me.emojicreator.net;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * author : dongSen
 * date : 2017/3/22 下午3:12
 * desc : 上传图片api
 */
public interface UploadApi {

    /**
     * 上传一张图片
     */
    @Multipart
    @POST("/api/add")
    Observable<String> uploadImage(@Part("name") RequestBody name,
                                   @Part("file\"; filename=\"image.png\"") RequestBody image);


}
