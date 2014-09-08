package it.cosenonjaviste.testableandroidapps.base;

import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.subjects.PublishSubject;

/**
 * Created by fabiocollini on 07/09/14.
 */
public class ObservableQueue<T> {
    private PublishSubject<Observable<T>> publishSubject = PublishSubject.create();
    private List<Observable<T>> runningObservables = new ArrayList<Observable<T>>();
    private Observable<T> lastCompleted;

    private boolean replayLast;

    public ObservableQueue(boolean replayLast) {
        this.replayLast = replayLast;
    }

    public void start(FragmentActivity activity, Observable<T> observable) {
        Observable<T> background = RxUtils.background(activity, observable.cache(1));
//        final Observable<T> cache = RxFragment.bindObservable(activity.getSupportFragmentManager(), background);
        startCacheObservable(background);
    }

    private void startCacheObservable(final Observable<T> observable) {
        runningObservables.add(observable);
        observable.subscribe(new Observer<T>() {
            @Override public void onCompleted() {
                lastCompleted = observable;
                runningObservables.remove(observable);
            }

            @Override public void onError(Throwable e) {
                runningObservables.remove(observable);
            }

            @Override public void onNext(T t) {

            }
        });
        publishSubject.onNext(observable);
//        final Observable<T> refCount2 = observable.doOnCompleted(new Action0() {
//            @Override public void call() {
//                lastCompleted = observable;
//                runningObservables.remove(observable);
//            }
//        }).doOnError(new Action1<Throwable>() {
//            @Override public void call(Throwable throwable) {
//                runningObservables.remove(observable);
//            }
//        });
//        publishSubject.onNext(refCount2);
    }

    public void start(final Observable<T> observable) {
        final Observable<T> cache = observable.cache(1);
        startCacheObservable(cache);
    }

    public Observable<Observable<T>> getObservable() {
        if (!runningObservables.isEmpty()) {
            return Observable.concat(Observable.from(runningObservables), publishSubject);
        } else if (lastCompleted != null && replayLast) {
            return Observable.concat(Observable.just(lastCompleted), publishSubject);
        } else {
            return publishSubject.asObservable();
        }
    }
}
