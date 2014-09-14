package it.cosenonjaviste.testableandroidapps.mvc.base;

import android.app.Activity;

import rx.Observable;
import rx.Scheduler;
import rx.android.observables.AndroidObservable;
import rx.schedulers.Schedulers;

/**
 * Created by fabiocollini on 25/08/14.
 */
public class RxUtils {
    private static Scheduler io = Schedulers.io();

    public static <T> Observable<T> background(Activity activity, Observable<T> observable) {
        return AndroidObservable.bindActivity(activity, observable.subscribeOn(io));
    }

    public static void setIo(Scheduler io) {
        RxUtils.io = io;
    }
}
