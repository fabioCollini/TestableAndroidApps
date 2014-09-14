package it.cosenonjaviste.testableandroidapps.base;

import java.util.ArrayList;
import java.util.List;

import it.cosenonjaviste.testableandroidapps.ObservableQueueItem;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.observers.Observers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by fabiocollini on 07/09/14.
 */
public class ObservableQueue<T> {
    private PublishSubject<ObservableQueueItem<T>> publishSubject = PublishSubject.create();
    private List<ObservableQueueItem<T>> runningObservables = new ArrayList<ObservableQueueItem<T>>();
    private ObservableQueueItem<T> lastCompleted;
    private Throwable lastThrowable;

    private boolean replayLast;

    public ObservableQueue(boolean replayLast) {
        this.replayLast = replayLast;
    }

    public void onNext(T item, final ConnectableObservable<T> observable) {
        final ObservableQueueItem<T> observableQueueItem = new ObservableQueueItem<T>(item, observable);
        runningObservables.add(observableQueueItem);
        observable.subscribe(new Observer<T>() {
            @Override public void onCompleted() {
                lastCompleted = observableQueueItem;
                lastThrowable = null;
                runningObservables.remove(observableQueueItem);
            }

            @Override public void onError(Throwable e) {
                lastThrowable = e;
                runningObservables.remove(observableQueueItem);
            }

            @Override public void onNext(T t) {

            }
        });
        publishSubject.onNext(observableQueueItem);
    }

    public Subscription subscribe(final Action0 onStart, Action1<? super T> onNext, Action1<Throwable> onError) {
        return subscribe(onStart, Observers.create(onNext, onError));
    }

    public Subscription subscribe(final Action0 onStart, final Observer<T> observer) {
        final CompositeSubscription subscriptions = new CompositeSubscription();
        subscriptions.add(asObservable().subscribe(new Action1<ObservableQueueItem<T>>() {
            @Override public void call(ObservableQueueItem<T> listObservable) {
                onStart.call();
                subscriptions.add(listObservable.getObservable().subscribe(observer));
            }
        }));

        return subscriptions;
    }

    public <R> Subscription subscribe(final Func1<ObservableQueueItem<T>, Observable<R>> onStart, final Observer<R> observer) {
        final CompositeSubscription subscriptions = new CompositeSubscription();
        subscriptions.add(asObservable().subscribe(new Action1<ObservableQueueItem<T>>() {
            @Override public void call(ObservableQueueItem<T> item) {
                Observable<R> observable = onStart.call(item);
                subscriptions.add(observable.subscribe(observer));
            }
        }));

        return subscriptions;
    }

    public Observable<ObservableQueueItem<T>> asObservable() {
        if (!runningObservables.isEmpty()) {
            return Observable.concat(Observable.from(runningObservables), publishSubject);
        } else if (lastCompleted != null && replayLast) {
            return Observable.concat(Observable.just(lastCompleted), publishSubject);
//        } else if (lastThrowable != null && replayLast) {
//            Observable<Observable<ObservableQueue<T>>> just = Observable.just(Observable.<ObservableQueue<T>>error(lastThrowable));
//            Observable<? extends Observable<? extends ObservableQueueItem<T>>> aaa = null;
//            return Observable.concat(aaa, publishSubject);
        } else {
            return publishSubject;
        }
    }
}
