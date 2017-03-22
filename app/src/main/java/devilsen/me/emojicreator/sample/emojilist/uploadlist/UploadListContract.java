package devilsen.me.emojicreator.sample.emojilist.uploadlist;

import java.util.List;

import devilsen.me.emojicreator.BasePresenter;
import devilsen.me.emojicreator.BaseView;
import devilsen.me.emojicreator.data.source.uploadlist.UploadImageBean;

/**
 * 上传列表contract
 */
public interface UploadListContract {

    interface View extends BaseView<Presenter> {

        void loadImages();

        void showImages(List<UploadImageBean> imageBeen);

        void showEmpty(boolean isEmpty);

        void stopFresh();
    }

    interface Presenter extends BasePresenter {

        void loadImages();
    }


}
