package it.cosenonjaviste.testableandroidapps.mvc.base;

import rx.Observable;
import rx.Observer;

/**
 * Created by fabiocollini on 05/10/14.
 */
public class PausableSubscriptions {
    public static <T> PausableSubscription subscribe(Observable<T> observable, Observer<T> observer) {
        return new PausableSubscriptionImpl<>(observable, observer);
    }
}
