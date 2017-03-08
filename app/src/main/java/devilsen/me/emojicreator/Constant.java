package devilsen.me.emojicreator;

/**
 * author : dongSen
 * date : 2017-02-27 17:49
 * desc :
 */
public class Constant {

    public static final int OFF_SET = 20;

//    public static final String SUFFIX = "?w=200&h=400";
    public static final String SUFFIX = "";

    /**
     * when instance fragment the key of the bundle
     */
    public static final String BUNDLE_TYPE = "bundle_type";

    //手气不错
    public static final int TYPE_LUCK = 10;
    //热门
    public static final int TYPE_HOT = 20;
    //本地表情
    public static final int TYPE_LOCAL = 30;
    //全部表情
    public static final int TYPE_ALL = 40;

    public static String getNameByType(int type) {
        String typeName;
        switch (type) {
            case TYPE_LUCK:
                typeName = "goodLuck";
                break;
            case TYPE_HOT:
                typeName = "hot";
                break;
            case TYPE_LOCAL:
                typeName = "local";
                break;
            case TYPE_ALL:
                typeName = "all";
                break;
            default:
                typeName = "error";
                break;
        }
        return typeName;
    }
}
