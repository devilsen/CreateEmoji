package devilsen.me.emojicreator.sample;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * author : dongSen
 * date : 2017-02-22 17:07
 * desc :
 */
public class BaseFragment extends Fragment {

    protected Context mContext;
    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }


}
