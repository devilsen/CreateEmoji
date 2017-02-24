package devilsen.me.emojicreator.util.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author : dongSen
 * date : 2017-02-23 15:05
 * desc : provider rx schedulers
 */
public class SchedulersProvider implements BaseSchedulersProvider {

    private SchedulersProvider() {
    }

    public static SchedulersProvider getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    private static class SingleTonHolder {
        private static final SchedulersProvider INSTANCE = new SchedulersProvider();
    }


    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
