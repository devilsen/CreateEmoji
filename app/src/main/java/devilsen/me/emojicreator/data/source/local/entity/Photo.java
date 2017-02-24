package devilsen.me.emojicreator.data.source.local.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.DATA;

/**
 * author : dongSen
 * date : 2017/2/24 下午6:34
 * desc : 获取图片的bean
 */
public class Photo implements Parcelable {

    private int id;
    private String path;
    private String directory;

    public Photo(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public Photo(int id, String path, String directory) {
        this.id = id;
        this.path = path;
        this.directory = directory;
    }

    public Photo() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;

        return id == photo.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static final Creator CREATOR = new Creator<Photo>() {

        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source.readInt(), source.readString(), source.readString());
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[0];
        }
    };

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(directory);
        dest.writeString(path);
    }

    public static Photo from(Cursor cursor) {
        Photo photo = new Photo();
        photo.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
        photo.directory = cursor.getString(cursor.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
        photo.path = cursor.getString(cursor.getColumnIndexOrThrow(DATA));
        return photo;
    }
}
