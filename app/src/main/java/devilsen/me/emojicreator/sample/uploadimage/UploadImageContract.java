package devilsen.me.emojicreator.sample.uploadimage;

import android.content.Context;
import android.widget.ImageView;

import devilsen.me.emojicreator.BasePresenter;
import devilsen.me.emojicreator.BaseView;

/**
 * author : dongSen
 * date : 2017/3/21 下午2:37
 * desc : 上传图片界面协议
 */
public interface UploadImageContract {

    interface View extends BaseView<Presenter> {
        void loadImage(String imagePath);

        void uploadImage();

        void changeImage();

        void finishView();
    }


    interface Presenter extends BasePresenter {
        void uploadImage(String imagePath, String name);

        void loadImage(Context context, ImageView mImage, String imagePath);
    }


}
