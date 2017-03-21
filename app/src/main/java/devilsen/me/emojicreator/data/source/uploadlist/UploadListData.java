package devilsen.me.emojicreator.data.source.uploadlist;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * 上传列表数据源
 */
public interface UploadListData {

    rx.Observable<List<UploadImageBean>> getUploadList();

    void uploadImage(@NonNull UploadImageBean imageBean);

    void deleteImage(@NonNull UploadImageBean imageBean);

    void updateImage(@NonNull UploadImageBean imageBean);

}
