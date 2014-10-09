package it.cosenonjaviste.testableandroidapps.base;

import android.support.v4.app.FragmentActivity;

import it.cosenonjaviste.testableandroidapps.mvc.base.ContextBinder;
import rx.Observable;

/**
 * Created by fabiocollini on 14/09/14.
 */
public class ActivityContextBinder implements ContextBinder {
    private FragmentActivity activity;

    public ActivityContextBinder(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override public <T> Observable<T> bindObservable(Observable<T> observable) {
        return RxUtils.background(activity, observable);
    }
}
