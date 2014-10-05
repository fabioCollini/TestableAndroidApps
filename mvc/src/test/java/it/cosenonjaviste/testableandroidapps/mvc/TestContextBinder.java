package it.cosenonjaviste.testableandroidapps.mvc;

import it.cosenonjaviste.testableandroidapps.mvc.base.ContextBinder;
import rx.Observable;
import rx.observables.ConnectableObservable;

/**
 * Created by fabiocollini on 14/09/14.
 */
public class TestContextBinder implements ContextBinder {

    @Override public <T> Observable<T> bindObservable(Observable<T> observable) {
        ConnectableObservable<T> publish = observable.publish();
        publish.connect();
        return publish;
    }
}
