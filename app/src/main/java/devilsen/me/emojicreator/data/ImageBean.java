package devilsen.me.emojicreator.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author : dongSen
 * date : 2017-02-22 15:51
 * desc :
 */
public class ImageBean implements Parcelable{

    public String _id;
    public String name;
    public String path;
    public String filename;
    public String add_time;
    public int views;
    public SizeBean size;

    public ImageBean(){
    }

    protected ImageBean(Parcel in) {
        _id = in.readString();
        name = in.readString();
        path = in.readString();
        filename = in.readString();
        add_time = in.readString();
        views = in.readInt();
    }

    public ImageBean(String path, String _id) {
        this.path = path;
        this._id = _id;
    }

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel in) {
            return new ImageBean(in);
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(name);
        dest.writeString(path);
        dest.writeString(filename);
        dest.writeString(add_time);
        dest.writeInt(views);
    }

    public static class SizeBean{
        public int width;
        public int height;

        public SizeBean(int width,int height) {
            this.width = width;
            this.height = height;
        }
    }
    
}

