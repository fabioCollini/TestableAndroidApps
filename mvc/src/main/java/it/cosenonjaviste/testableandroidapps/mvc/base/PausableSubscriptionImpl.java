package it.cosenonjaviste.testableandroidapps.mvc.base;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

/**
 * Created by fabiocollini on 05/10/14.
 */
public class PausableSubscriptionImpl<T> implements PausableSubscription {

    private final PublishSubject<T> subject;

    private final Observer<T> observer;

    private Subscription mainSubscription;

    private Subscription innerSubscription;

    private Subscription replaySubjectSubscription;

    private ReplaySubject<T> replaySubject;

    public PausableSubscriptionImpl(Observable<T> observable, Observer<T> observer) {
        this.observer = observer;
        subject = PublishSubject.create();
        mainSubscription = observable.subscribe(subject);
        innerSubscription = subject.subscribe(observer);
    }

    @Override public void pause() {
        System.out.println("pause");
        innerSubscription.unsubscribe();
        if (replaySubject == null) {
            replaySubject = ReplaySubject.create();
            replaySubjectSubscription = subject.subscribe(replaySubject);
        }
    }

    @Override public void resume() {
        System.out.println("resume");
        replaySubjectSubscription.unsubscribe();
        innerSubscription = Observable
                .merge(replaySubject, subject)
                .doOnEach(notification -> System.out.println("Concat " + notification))
                .subscribe(observer);
    }

    @Override public void destroy() {
        if (!innerSubscription.isUnsubscribed()) {
            innerSubscription.unsubscribe();
        }
        mainSubscription.unsubscribe();
    }
}
