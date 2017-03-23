package devilsen.me.emojicreator.sample.uploadimage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import devilsen.me.emojicreator.Injection;
import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.data.source.uploadlist.UploadImageBean;
import devilsen.me.emojicreator.data.source.uploadlist.UploadListData;
import devilsen.me.emojicreator.net.upload.UploadImageClient;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * author : dongSen
 * date : 2017/3/21 下午2:42
 * desc : 上传图片presenter
 */
public class UploadPresenter implements UploadImageContract.Presenter {

    private UploadImageContract.View mView;

    private Context mContext;

    private UploadListData mUploadListData;


    public UploadPresenter(@NonNull Context context,
                           @NonNull UploadImageContract.View mView) {
        mContext = context;

        this.mView = checkNotNull(mView);

        mUploadListData = Injection.provideUploadRepository(context);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void uploadImage(String imagePath, String name) {
        UploadImageClient.getInstance().upload(name, imagePath);
        mView.finishView();

        mUploadListData.uploadImage(new UploadImageBean(name, imagePath, true));

    }

    @Override
    public void loadImage(Context context, ImageView mImage, String imagePath) {
        Glide.with(context)
                .load(imagePath)
                .dontAnimate()
                .fitCenter()
                .thumbnail(0.5f)
                .placeholder(R.mipmap.ic_image_holder)
                .into(mImage);
    }

}
