package it.cosenonjaviste.testableandroidapps.mvc.base;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.ReplaySubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by fabiocollini on 05/10/14.
 */
public class ObservableQueueState<T> {
    private List<ObservableQueueItem<T>> pauseItems = new ArrayList<>();

    private CompositeSubscription subscriptions = new CompositeSubscription();

    public void pause(List<ObservableQueueItem<T>> runningObservables) {
        for (ObservableQueueItem<T> item : runningObservables) {
            ReplaySubject<T> replaySubject = ReplaySubject.create();
            subscriptions.add(item.getObservable().subscribe(replaySubject));
            ObservableQueueItem<T> replayItem = new ObservableQueueItem<>(item.getItem(), replaySubject);
            pauseItems.add(replayItem);
        }
    }

    public Observable<ObservableQueueItem<T>> getAndClearObservable() {
        Observable<ObservableQueueItem<T>> ret = Observable.from(pauseItems);
        pauseItems = new ArrayList<>();
        subscriptions.unsubscribe();
        return ret;
    }

    @Override public String toString() {
        return "ObservableQueueState{" +
                "pauseItems=" + pauseItems +
                '}';
    }
}
