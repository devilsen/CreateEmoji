package devilsen.me.emojicreator.sample.createemoji;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import devilsen.me.emojicreator.R;


/**
 * Description : 颜色选择adapter
 * author : dongsen
 * Time : 2016-02-27
 */
public class SelectColorAdapter extends RecyclerView.Adapter<SelectColorAdapter.ViewHolder> {

    private Resources resources;
    private Context mContext;
    private static SelectColorListener colorListener;
    private static CheckBox lastCheckbox;
    public static int[] colorCollection = new int[]{
            R.color.select_color_1,
            R.color.select_color_2,
            R.color.select_color_3,
            R.color.select_color_4,
            R.color.select_color_5,
            R.color.select_color_6,
            R.color.select_color_7,
            R.color.select_color_8,
            R.color.select_color_9,
            R.color.select_color_10,
            R.color.select_color_11,
            R.color.select_color_12
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_select_color, parent, false);
        resources = mContext.getResources();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.colorView.setBackgroundColor(resources.getColor(colorCollection[position]));
    }

    @Override
    public int getItemCount() {
        return colorCollection.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View colorView;
        private CheckBox colorCb;

        public ViewHolder(View itemView) {
            super(itemView);
            colorView = itemView.findViewById(R.id.select_color_view);
            colorCb = (CheckBox) itemView.findViewById(R.id.select_color_cb);
            colorCb.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.select_color_cb:
                    if (lastCheckbox != null && lastCheckbox != v)
                        lastCheckbox.setChecked(false);

                    if (colorListener != null)
                        colorListener.getSelectColor(colorCollection[getAdapterPosition()]);

                    lastCheckbox = (CheckBox) v;
                    break;
            }
        }
    }

    public void setColorListener(SelectColorListener listener) {
        colorListener = listener;
    }

    public interface SelectColorListener {
        void getSelectColor(int colorId);
    }


}
