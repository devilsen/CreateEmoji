package devilsen.me.emojicreator.util.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * author : dongSen
 * date : 2017-02-23 15:03
 * desc : provide immediate schedulers
 */
public class ImmediateSchedulersProvider implements BaseSchedulersProvider {
    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.immediate();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.immediate();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return Schedulers.immediate();
    }
}
