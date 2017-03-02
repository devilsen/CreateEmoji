package devilsen.me.emojicreator.sample.search;

import java.util.List;

import devilsen.me.emojicreator.BasePresenter;
import devilsen.me.emojicreator.BaseView;
import devilsen.me.emojicreator.data.ImageBean;

/**
 * author : dongSen
 * date : 2017-03-02 16:43
 * desc :
 */
public interface SearchEmojiContract {

    interface View extends BaseView<Presenter> {

        void showEmojis(List<ImageBean> listData);

        void loadImage(String keyword, boolean isFresh);

        void stopFresh();

    }

    interface Presenter extends BasePresenter {

        void loadEmojis(String keyword, int page);

    }


}
