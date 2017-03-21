package devilsen.me.emojicreator.data.source.uploadlist;

import java.util.UUID;

/**
 * author : dongSen
 * date : 2017/3/17 下午6:38
 * desc : 上传图片bean
 */
public class UploadImageBean {

    public String id;
    public String name;
    public String path;
    public boolean status;

    public UploadImageBean(String name, String path, boolean status) {
        this(UUID.randomUUID().toString(), name, path, status);
    }

    public UploadImageBean(String id, String name, String path, boolean status) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.status = status;
    }
}
