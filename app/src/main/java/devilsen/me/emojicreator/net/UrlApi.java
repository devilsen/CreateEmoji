package devilsen.me.emojicreator.net;

import java.util.List;

import devilsen.me.emojicreator.data.ImageBean;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * author : dongSen
 * date : 16/6/3 下午5:02
 * desc :
 */
public interface UrlApi {

    //全部图片列表
    @GET("list")
    Observable<List<ImageBean>> getList(@Query("begin") int begin, @Query("offset") int offset);

    //热门列表
    @GET("list/hot")
    Observable<List<ImageBean>> getHotList(@Query("begin") int begin, @Query("offset") int offset);

    //推荐列表
    @GET("list/recommend")
    Observable<List<ImageBean>> getRecommendList(@Query("begin") int begin, @Query("offset") int offset);

    //手气不错
    @GET("goodluck")
    Observable<List<ImageBean>> getGoodLuckList(@Query("begin") int begin, @Query("offset") int offset);

    //搜索列表
    @GET("search/{keyword}")
    Observable<List<ImageBean>> getSearchList(@Path("keyword") String keyword, @Query("begin") int begin, @Query("offset") int offset);

}
