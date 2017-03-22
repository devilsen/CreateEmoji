package devilsen.me.emojicreator.sample.uploadimage;

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

import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.data.source.uploadlist.UploadImageBean;
import devilsen.me.emojicreator.util.ImageSizeUtil;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Description : item adapter
 * author : dongsen
 * Time : 2016-02-26
 */
public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    private List<UploadImageBean> listData;

    private ItemClickListener itemClickListener;

    public UploadListAdapter(Fragment context) {
        this.listData = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_source, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        UploadImageBean bean = listData.get(position);
        if (bean == null)
            return;

        Uri uri;

        ImageSizeUtil.ImageSize imageSize = ImageSizeUtil.getInstance().decodeImageSize(bean.path);
        uri = Uri.fromFile(new File(bean.path));

        holder.nameTxt.setText(bean.name);

        float ratio = (float) imageSize.width / (float) imageSize.height;
        holder.sourceImg.setAspectRatio(ratio);

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

    public void replaceData(List<UploadImageBean> listData) {
        this.listData = checkNotNull(listData);
        notifyDataSetChanged();
    }

    public void addData(List<UploadImageBean> listData) {
        this.listData.addAll(listData);
        notifyDataSetChanged();
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
        void onItemClick(UploadImageBean bean, View imageView);

        void onItemLongClick(UploadImageBean bean, int position);
    }


}