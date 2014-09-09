package it.cosenonjaviste.testableandroidapps.base;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.observables.ConnectableObservable;
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

    public void onNext(final ConnectableObservable<T> observable) {
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
    }

    public Observable<Observable<T>> asObservable() {
        if (!runningObservables.isEmpty()) {
            return Observable.concat(Observable.from(runningObservables), publishSubject);
        } else if (lastCompleted != null && replayLast) {
            return Observable.concat(Observable.just(lastCompleted), publishSubject);
        } else {
            return publishSubject.asObservable();
        }
    }
}
