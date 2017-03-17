package devilsen.me.emojicreator.data.source.upload;

import android.database.Observable;

import java.util.List;

/**
 * 上传列表数据源
 */
public interface UploadDataSource {

    Observable<List<UploadImageBean>> getUploadList(int page);

    void uploadImage(UploadImageBean imageBean);

    void deleteImage(UploadImageBean imageBean);

    void updateImage(UploadImageBean imageBean);

}
