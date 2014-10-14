package it.cosenonjaviste.testableandroidapps.mvc.base.pausable;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

class PausableSubscriptionImpl<T> extends PausableSubscriptionAdapter<T> implements PausableSubscription {

    private final Subject<T, T> subject;

    private Subscription replaySubjectSubscription;

    private ReplaySubject<T> replaySubject;

    public PausableSubscriptionImpl(Observable<T> observable, Action0 onAttach, Observer<T> observer) {
        super(onAttach, observer);
        subject = PublishSubject.create();
        innerSubscription = subscribeInnerObservable(onAttach, subject, observer);
        mainSubscription = observable.subscribe(subject);
    }

    @Override public void pause() {
        if (state == RUNNING) {
            synchronized (this) {
                if (state == RUNNING) {
                    replaySubject = ReplaySubject.create();
                    replaySubjectSubscription = subject.subscribe(replaySubject);
                    super.pause();
                }
            }
        }
    }

    @Override public void resume(Action0 onAttach, Observer<?> observer) {
        if (state == PAUSED) {
            synchronized (this) {
                if (state == PAUSED) {
                    replaySubjectSubscription.unsubscribe();
                    replaySubject.onCompleted();
                    Observable<T> newObservable = Observable.concat(replaySubject, subject);
                    replaySubject = null;
                    innerSubscription = subscribeInnerObservable(onAttach, newObservable, (Observer<? super T>) observer);
                }
            }
        }
    }
}
