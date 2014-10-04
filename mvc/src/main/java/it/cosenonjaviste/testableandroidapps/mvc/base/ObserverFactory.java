package it.cosenonjaviste.testableandroidapps.mvc.base;

import rx.Observable;
import rx.Subscription;

/**
 * Created by fabiocollini on 04/10/14.
 */
public interface ObserverFactory<P, T> {
    Subscription subscribe(P item, Observable<T> observable);
}
