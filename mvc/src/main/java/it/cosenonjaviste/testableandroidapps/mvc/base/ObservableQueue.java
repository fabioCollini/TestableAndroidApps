package it.cosenonjaviste.testableandroidapps.mvc.base;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by fabiocollini on 07/09/14.
 */
public class ObservableQueue<T> {
    private PublishSubject<ObservableQueueItem<T>> publishSubject = PublishSubject.create();
    private List<ObservableQueueItem<T>> runningObservables = new ArrayList<ObservableQueueItem<T>>();

    public void onNext(T item, final Observable<T> observable) {
        final ObservableQueueItem<T> observableQueueItem = new ObservableQueueItem<T>(item, observable);
        runningObservables.add(observableQueueItem);
        observable.subscribe(new Observer<T>() {
            @Override public void onCompleted() {
                runningObservables.remove(observableQueueItem);
            }

            @Override public void onError(Throwable e) {
                runningObservables.remove(observableQueueItem);
            }

            @Override public void onNext(T t) {

            }
        });
        publishSubject.onNext(observableQueueItem);
    }

    public Subscription subscribe(ObserverFactory<T, T> observerFactory) {
        final CompositeSubscription subscriptions = new CompositeSubscription();
        subscriptions.add(asObservable().subscribe(item -> {
            Subscription s = observerFactory.subscribe(item.getItem(), item.getObservable());
            subscriptions.add(s);
        }));

        return subscriptions;
    }

    public Observable<ObservableQueueItem<T>> asObservable() {
        if (!runningObservables.isEmpty()) {
            return Observable.concat(Observable.from(runningObservables), publishSubject);
        } else {
            return publishSubject;
        }
    }
}
