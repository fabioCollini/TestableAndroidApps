package it.cosenonjaviste.testableandroidapps.base;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;
import rx.observers.Observers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by fabiocollini on 07/09/14.
 */
public class ObservableQueue<T> {
    private PublishSubject<Observable<T>> publishSubject = PublishSubject.create();
    private List<Observable<T>> runningObservables = new ArrayList<Observable<T>>();
    private Observable<T> lastCompleted;
    private Throwable lastThrowable;

    private boolean replayLast;

    public ObservableQueue(boolean replayLast) {
        this.replayLast = replayLast;
    }

    public void onNext(final ConnectableObservable<T> observable) {
        runningObservables.add(observable);
        observable.subscribe(new Observer<T>() {
            @Override public void onCompleted() {
                lastCompleted = observable;
                lastThrowable = null;
                runningObservables.remove(observable);
            }

            @Override public void onError(Throwable e) {
                lastThrowable = e;
                runningObservables.remove(observable);
            }

            @Override public void onNext(T t) {

            }
        });
        publishSubject.onNext(observable);
    }

    public Subscription subscribe(final Action0 onStart, Action1<? super T> onNext, Action1<Throwable> onError) {
        return subscribe(onStart, Observers.create(onNext, onError));
    }

    public Subscription subscribe(final Action0 onStart, final Observer<T> observer) {
        final CompositeSubscription subscriptions = new CompositeSubscription();
        subscriptions.add(asObservable().subscribe(new Action1<Observable<T>>() {
            @Override public void call(Observable<T> listObservable) {
                onStart.call();
                subscriptions.add(listObservable.subscribe(observer));
            }
        }));

        return subscriptions;
    }

    public Observable<Observable<T>> asObservable() {
        if (!runningObservables.isEmpty()) {
            return Observable.concat(Observable.from(runningObservables), publishSubject);
        } else if (lastCompleted != null && replayLast) {
            return Observable.concat(Observable.just(lastCompleted), publishSubject);
        } else if (lastThrowable != null && replayLast) {
            return Observable.concat(Observable.just(Observable.<T>error(lastThrowable)), publishSubject);
        } else {
            return publishSubject.asObservable();
        }
    }
}
