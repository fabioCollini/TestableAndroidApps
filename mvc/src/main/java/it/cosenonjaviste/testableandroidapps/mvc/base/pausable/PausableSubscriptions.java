package it.cosenonjaviste.testableandroidapps.mvc.base.pausable;

import rx.Observable;
import rx.Observer;
import rx.functions.Action0;

public class PausableSubscriptions {
    public static <T> PausableSubscription subscribe(Observable<T> observable, Action0 onAttach, Observer<T> observer) {
        return new PausableSubscriptionImpl<T>(observable, onAttach, observer);
    }

    public static <T> PausableSubscription subscribe(Observable<T> observable, Observer<T> observer) {
        return subscribe(observable, null, observer);
    }

    public static <T> PausableSubscription subscribe(Observable<T> observable, Action0 onAttach, Observer<T> observer, int replay) {
        return new PausableSubscriptionReplayImpl<T>(observable, onAttach, observer, replay);
    }

    public static <T> PausableSubscription subscribe(Observable<T> observable, Observer<T> observer, int replay) {
        return subscribe(observable, null, observer, replay);
    }
}
