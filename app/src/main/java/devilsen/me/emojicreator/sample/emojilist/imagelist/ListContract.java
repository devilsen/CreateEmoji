package devilsen.me.emojicreator.sample.emojilist.imagelist;

import java.util.List;

import devilsen.me.emojicreator.BasePresenter;
import devilsen.me.emojicreator.BaseView;
import devilsen.me.emojicreator.data.ImageBean;

/**
 * 图片列表接口
 */
public interface ListContract {

    interface View extends BaseView<Presenter> {

        void showEmpty(boolean showEmpty);

        void showEmojis(List<ImageBean> listData);

        void loadImage(boolean isFresh);

        void stopFresh();

        /**
         * 进入创建表情页面
         */
        void createEmoji(ImageBean imageBean);

        void showLongClickDialog(ImageBean bean, int position);
    }

    interface Presenter extends BasePresenter {

        /**
         * 加载图片列表
         *
         * @param type 列表类型
         * @param page 第几页
         */
        void loadList(int type, int page);

    }


}
