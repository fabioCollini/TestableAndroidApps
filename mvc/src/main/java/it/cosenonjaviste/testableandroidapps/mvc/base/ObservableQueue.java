package it.cosenonjaviste.testableandroidapps.mvc.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, ObservableQueueState<T>> state = new HashMap<>();

    public void onNext(T item, final Observable<T> observable) {
        System.out.println("ObservableQueue.onNext " + runningObservables);
        final ObservableQueueItem<T> observableQueueItem = new ObservableQueueItem<T>(item, observable);
        runningObservables.add(observableQueueItem);
        observable.subscribe(new Observer<T>() {
            @Override public void onCompleted() {
                runningObservables.remove(observableQueueItem);
                System.out.println("ObservableQueue.onCompleted " + runningObservables);
            }

            @Override public void onError(Throwable e) {
                runningObservables.remove(observableQueueItem);
            }

            @Override public void onNext(T t) {

            }
        });
        publishSubject.onNext(observableQueueItem);
    }

    public Subscription subscribe(String key, ObserverFactory<T, T> observerFactory) {
        final CompositeSubscription subscriptions = new CompositeSubscription();
        subscriptions.add(asObservable(key).subscribe(item -> {
            Subscription s = observerFactory.subscribe(item.getItem(), item.getObservable());
            subscriptions.add(s);
        }));

        return subscriptions;
    }

    public Observable<ObservableQueueItem<T>> asObservable(String key) {
        System.out.println("ObservableQueue.asObservable " + state + " running " + runningObservables);
        Observable<ObservableQueueItem<T>> ret = publishSubject;
        if (!runningObservables.isEmpty()) {
            ret = Observable.concat(Observable.from(runningObservables), ret);
        }
        ObservableQueueState<T> s = state.get(key);
        if (s != null) {
            ret = Observable.concat(s.getAndClearObservable(), ret);
        }
        return ret;
    }

    public void pause(String key) {
        ObservableQueueState<T> s = new ObservableQueueState<>();
        s.pause(runningObservables);
        state.put(key, s);
    }

    public void destroyCache(String key) {
        ObservableQueueState<T> s = state.get(key);
        if (s != null) {
            s.getAndClearObservable();
            state.remove(key);
        }
    }

    public boolean isCacheEmpty() {
        return state.isEmpty();
    }

    public int getRunningObservableCount() {
        return runningObservables.size();
    }
}
