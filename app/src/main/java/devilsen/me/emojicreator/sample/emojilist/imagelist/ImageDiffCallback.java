package devilsen.me.emojicreator.sample.emojilist.imagelist;

import android.support.v7.util.DiffUtil;

import java.util.List;

import devilsen.me.emojicreator.data.ImageBean;

/**
 * author : dongSen
 * date : 2017-02-28 14:35
 * desc :
 */
public class ImageDiffCallback extends DiffUtil.Callback {

    private List<ImageBean> oldList;
    private List<ImageBean> newList;

    public ImageDiffCallback(List<ImageBean> newList, List<ImageBean> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList == null ? 0 : oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList == null ? 0 : newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) != null && oldList.get(oldItemPosition)._id.equals(newList.get(newItemPosition)._id);

    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldList.get(oldItemPosition).filename == null)
            return false;

        return oldList.get(oldItemPosition) != null && oldList.get(oldItemPosition).filename.equals(newList.get(newItemPosition).filename);
    }
}
