package it.cosenonjaviste.testableandroidapps.mvc.base;

import rx.Observable;

/**
 * Created by fabiocollini on 14/09/14.
 */
public class ObservableQueueItem<T> {
    private T item;

    private Observable<T> observable;

    public ObservableQueueItem(T item, Observable<T> observable) {
        this.item = item;
        this.observable = observable;
    }

    public T getItem() {
        return item;
    }

    public Observable<T> getObservable() {
        return observable;
    }
}
