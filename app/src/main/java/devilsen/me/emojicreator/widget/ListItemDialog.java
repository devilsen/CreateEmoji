package devilsen.me.emojicreator.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.util.ScreenUtils;

/**
 * author : dongSen
 * date : 2017-03-01 17:32
 * desc :
 */
public class ListItemDialog extends Dialog {

    public ListItemDialog(Context context) {
        super(context);
    }

    public ListItemDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ListItemDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder implements View.OnClickListener {
        private Context mContext;

        private View mContentView;

        private ListItemDialog dialog;

        private TextView deleteTxt;

        private boolean isCancelable = true;

        private boolean isCanceledOnTouchOutside = true;

        private ListItemDialog.OnDialogClickListener mListener;

        public Builder(Context context) {
            mContext = context;
            LayoutInflater inflater = LayoutInflater.from(context);
            mContentView = inflater.inflate(R.layout.dialog_long_click_content, null);

            deleteTxt = (TextView) mContentView.findViewById(R.id.dialog_item_txt);

            deleteTxt.setOnClickListener(this);

        }

        public ListItemDialog create() {
            dialog = new ListItemDialog(mContext, R.style.CustomDialog);

            dialog.setContentView(mContentView);
            dialog.setCancelable(isCancelable);
            dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
            Window dialogWindow = dialog.getWindow();
            /*
             * 将对话框的大小按屏幕大小的百分比设置
			 */
            WindowManager.LayoutParams p; // 获取对话框当前的参数值
            if (dialogWindow != null) {
                p = dialogWindow.getAttributes();
                p.width = (int) (ScreenUtils.getScreenWidth(mContext) * 0.8); // 宽度设置为屏幕的0.65
                dialogWindow.setAttributes(p);
            }

            return dialog;
        }

        /**
         * 是否可以返回取消对话框
         */
        public ListItemDialog.Builder setCancelable(boolean able) {
            isCancelable = able;
            return this;
        }

        public ListItemDialog.Builder setCanceledOnTouchOutside(boolean able) {
            this.isCanceledOnTouchOutside = able;
            return this;
        }

        /**
         * @param listener
         * @return Builder
         */
        public ListItemDialog.Builder setOnDialogClickListener(ListItemDialog.OnDialogClickListener listener) {
            mListener = listener;
            return this;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.dialog_item_txt && mListener != null) {
                mListener.onClick(v);
                if (dialog != null)
                    dialog.dismiss();
            }
        }
    }

    public interface OnDialogClickListener {
        void onClick(View v);
    }
}
