package devilsen.me.emojicreator.data.source;

import java.util.List;

import devilsen.me.emojicreator.data.ImageBean;
import rx.Observable;

/**
 * Main entry point for accessing tasks data.
 * <p>
 */
public interface EmojiDataSource {

    Observable<List<ImageBean>> getList(int type, int page);

}
