package devilsen.me.emojicreator.data.source.upload;

import android.database.Observable;

import java.util.List;

/**
 * Created by Devilsen on 2017/3/17.
 */

public class UploadData implements UploadDataSource {


    @Override
    public Observable<List<UploadImageBean>> getUploadList(int page) {
        return null;
    }

    @Override
    public void uploadImage(UploadImageBean imageBean) {

    }

    @Override
    public void deleteImage(UploadImageBean imageBean) {

    }

    @Override
    public void updateImage(UploadImageBean imageBean) {

    }
}
