package it.cosenonjaviste.testableandroidapps.mvc.base.pausable;

import rx.Observable;
import rx.Observer;
import rx.functions.Action0;
import rx.subjects.ReplaySubject;

class PausableSubscriptionReplayImpl<T> extends PausableSubscriptionAdapter<T> implements PausableSubscription {

    private final ReplaySubject<T> subject;

    public PausableSubscriptionReplayImpl(Observable<T> observable, Action0 onAttach, Observer<T> observer, int replaySize) {
        super(onAttach, observer);
        subject = ReplaySubject.createWithSize(replaySize);
        innerSubscription = subscribeInnerObservable(onAttach, subject, observer);
        mainSubscription = observable.subscribe(subject);
    }

    @Override public void resume(Action0 onAttach, Observer<?> observer) {
        if (state == PAUSED) {
            synchronized (this) {
                if (state == PAUSED) {
                    innerSubscription = subscribeInnerObservable(onAttach, subject, (Observer<? super T>) observer);
                }
            }
        }
    }
}
