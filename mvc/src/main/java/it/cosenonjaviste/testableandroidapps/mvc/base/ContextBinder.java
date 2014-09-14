package it.cosenonjaviste.testableandroidapps.mvc.base;

import rx.Observable;
import rx.observables.ConnectableObservable;

/**
 * Created by fabiocollini on 14/09/14.
 */
public interface ContextBinder {
    <T> ConnectableObservable<T> bindObservable(Observable<T> observable);
}
