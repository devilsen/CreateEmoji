package devilsen.me.emojicreator.sample.emojilist.imagelist;

import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import devilsen.me.emojicreator.Constant;
import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.data.ImageBean;
import devilsen.me.emojicreator.task.ApiService;
import devilsen.me.emojicreator.util.ImageSizeUtil;
import devilsen.me.emojicreator.widget.RatioImageView;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Description : item adapter
 * author : dongsen
 * Time : 2016-02-26
 */
public class SourceListAdapter extends RecyclerView.Adapter<SourceListAdapter.ViewHolder> {

    private List<ImageBean> listData;
    private List<ImageBean> newList;

    private Fragment mContext;
    private ItemClickListener itemClickListener;

    public SourceListAdapter(Fragment context) {
        mContext = context;
        this.listData = new ArrayList<>();
        this.newList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_source, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ImageBean bean = listData.get(position);
        if (bean == null)
            return;

        String path;

        if (bean.path.startsWith("http:")) {
            path = bean.path + Constant.SUFFIX;

            holder.sourceImg.setOriginalSize(bean.size.width, bean.size.height);
        } else if (bean.path.startsWith("/api")) {
            path = ApiService.HOST + bean.path + Constant.SUFFIX;

            holder.sourceImg.setOriginalSize(bean.size.width, bean.size.height);
        } else {
            ImageSizeUtil.getInstance().decodeImageAndSetSize(bean.path, holder.sourceImg);
            path = bean.path;
        }

        holder.nameTxt.setText(bean.name);

//        Glide.with(mContext)
//                .load(path)
////                .placeholder(R.mipmap.emoji_creator_icon)
//                .error(R.mipmap.emoji_creator_icon_2)
//                .thumbnail(0.5f)
//                .skipMemoryCache(true)
//                .fitCenter()
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.sourceImg);

        Glide.with(mContext)
                .load(path)
                .asBitmap()
                .error(R.mipmap.emoji_creator_icon_2)
                .skipMemoryCache(true)
                .fitCenter()
                .into(holder.sourceImg);

    }

    @Override
    public int getItemCount() {
        return listData == null ? 0 : listData.size();
    }

    public void setItemClickListener(ItemClickListener clickListener) {
        itemClickListener = clickListener;
    }

    public void replaceData(List<ImageBean> listData) {
        this.listData = checkNotNull(listData);
        notifyDataSetChanged();
    }

    public void addData(List<ImageBean> listData) {
        newList.addAll(listData);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ImageDiffCallback(this.listData, newList), true);

        this.listData.addAll(listData);
        diffResult.dispatchUpdatesTo(this);
//        notifyDataSetChanged();
    }

    /**
     * 删除图片
     *
     * @param position 图片位置
     */
    public void deleteItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        RatioImageView sourceImg;
        TextView nameTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            sourceImg = (RatioImageView) itemView.findViewById(R.id.list_item_img);
            nameTxt = (TextView) itemView.findViewById(R.id.list_item_name_txt);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onItemClick(listData.get(getAdapterPosition()), sourceImg);
        }

        @Override
        public boolean onLongClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onItemLongClick(listData.get(getAdapterPosition()), getAdapterPosition());
            return true;
        }
    }

    public interface ItemClickListener {
        void onItemClick(ImageBean bean, View imageView);

        void onItemLongClick(ImageBean bean, int position);
    }


}