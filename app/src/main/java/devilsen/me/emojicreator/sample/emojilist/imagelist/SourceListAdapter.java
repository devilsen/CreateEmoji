package devilsen.me.emojicreator.sample.emojilist.imagelist;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.data.ImageBean;
import devilsen.me.emojicreator.task.ApiService;
import devilsen.me.emojicreator.widget.RatioImageView;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Description : item adapter
 * author : dongsen
 * Time : 2016-02-26
 */
public class SourceListAdapter extends RecyclerView.Adapter<SourceListAdapter.ViewHolder> {

    private List<ImageBean> listPath;
    private Context mContext;
    private ItemClickListener itemClickListener;

    private BitmapFactory.Options options;


    public SourceListAdapter(ArrayList<ImageBean> listPath) {
        this.listPath = listPath;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_source, parent, false);
        mContext = parent.getContext();

        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ImageBean bean = listPath.get(position);
        String path;

        if (!bean.path.startsWith("/api")) {
            BitmapFactory.decodeFile(bean.path, options);

            holder.sourceImg.setOriginalSize(options.outWidth, options.outHeight);

            path = bean.path;
        } else {
            holder.sourceImg.setOriginalSize(bean.size.width, bean.size.height);

            path = ApiService.HOST + bean.path;
        }

        holder.nameTxt.setText(bean.name);

        Glide.with(mContext)
                .load(path)
//                .placeholder(R.mipmap.emoji_creator_icon)
                .error(R.mipmap.emoji_creator_icon_2)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.sourceImg);

    }

    @Override
    public int getItemCount() {
        return listPath == null ? 0 : listPath.size();
    }

    public void setItemClickListener(ItemClickListener clickListener) {
        itemClickListener = clickListener;
    }

    public void replaceData(List<ImageBean> listData) {
        listPath = checkNotNull(listData);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RatioImageView sourceImg;
        TextView nameTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            sourceImg = (RatioImageView) itemView.findViewById(R.id.list_item_img);
            nameTxt = (TextView) itemView.findViewById(R.id.list_item_name_txt);
            sourceImg.setOnClickListener(this);
            sourceImg.setOriginalSize(50, 50);

        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.itemClick(listPath.get(getAdapterPosition()), sourceImg);
        }
    }

    public interface ItemClickListener {
        void itemClick(ImageBean bean, View imageView);
    }


}