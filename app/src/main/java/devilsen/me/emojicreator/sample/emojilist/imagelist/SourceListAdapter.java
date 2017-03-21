package devilsen.me.emojicreator.sample.emojilist.imagelist;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import devilsen.me.emojicreator.Constant;
import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.data.ImageBean;
import devilsen.me.emojicreator.net.ApiService;
import devilsen.me.emojicreator.util.ImageSizeUtil;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Description : item adapter
 * author : dongsen
 * Time : 2016-02-26
 */
public class SourceListAdapter extends RecyclerView.Adapter<SourceListAdapter.ViewHolder> {

    private List<ImageBean> listData;
    private List<ImageBean> newList;

    private ItemClickListener itemClickListener;
//    private final GenericDraweeHierarchy hierarchy;

    public SourceListAdapter(Fragment context) {
        this.listData = new ArrayList<>();
        this.newList = new ArrayList<>();

//        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
//        hierarchy = builder
//                .setFadeDuration(300)
//                .setPlaceholderImage(R.mipmap.ic_image_holder)
//                .setFailureImage(R.mipmap.emoji_creator_icon_2)
//                .setProgressBarImage(new ProgressBarDrawable())
//                .build();
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

        Uri uri;

        if (bean.path.startsWith("https:")) {
            uri = Uri.parse(bean.path + Constant.SUFFIX);
        } else if (bean.path.startsWith("/api")) {
            uri = Uri.parse(ApiService.HOST + bean.path + Constant.SUFFIX);
        } else {
            ImageSizeUtil.getInstance().decodeImageAndSetSize(bean.path, bean);
            uri = Uri.fromFile(new File(bean.path));
        }

        holder.nameTxt.setText(bean.name);

//        Glide.with(mContext)
//                .load(path)
//                .placeholder(R.mipmap.ic_image_holder)
//                .error(R.mipmap.emoji_creator_icon_2)
//                .thumbnail(0.5f)
//                .skipMemoryCache(true)
//                .fitCenter()
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.sourceImg);

        float ratio = (float) bean.size.width / (float) bean.size.height;
        holder.sourceImg.setAspectRatio(ratio);
//        holder.sourceImg.setHierarchy(hierarchy);
//        sourceImg.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        holder.sourceImg.setController(controller);

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
//        newList.addAll(listData);

//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ImageDiffCallback(this.listData, newList), true);

        this.listData.addAll(listData);
        notifyDataSetChanged();
//        diffResult.dispatchUpdatesTo(this);
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

    /**
     * 获取图片的数量
     */
    public int getListSize() {
        return listData == null ? 0 : listData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        SimpleDraweeView sourceImg;
        TextView nameTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            sourceImg = (SimpleDraweeView) itemView.findViewById(R.id.list_item_img);
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