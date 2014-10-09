package it.cosenonjaviste.testableandroidapps.mvc.base.pausable;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
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

    private Action1<PausableSubscription> onDestroyCallback;

    public PausableSubscriptionImpl(Observable<T> observable, Observer<T> observer) {
        this.observer = observer;
        subject = PublishSubject.create();
        innerSubscription = subject.subscribe(observer);
        mainSubscription = observable.subscribe(subject);
    }

    @Override public void pause() {
        if (innerSubscription != null) {
            innerSubscription.unsubscribe();
            innerSubscription = null;
        }
        if (replaySubject == null && !mainSubscription.isUnsubscribed()) {
            replaySubject = ReplaySubject.create();
            replaySubjectSubscription = subject.subscribe(replaySubject);
        }
    }

    @Override public void resume() {
        resume(observer);
    }

    @Override public void resume(Observer<?> observer) {
        if (innerSubscription == null && replaySubject != null) {
            replaySubjectSubscription.unsubscribe();
            replaySubject.onCompleted();
            Observable<T> newObservable = Observable.concat(replaySubject, subject);
            //TODO
            replaySubject = null;
            innerSubscription = newObservable
                    .doOnEach(notification -> System.out.println("Concat " + notification))
                    .subscribe((Observer<? super T>) observer);
        }
    }

    @Override public void destroy() {
        if (innerSubscription != null && !innerSubscription.isUnsubscribed()) {
            innerSubscription.unsubscribe();
        }
        mainSubscription.unsubscribe();
        if (onDestroyCallback != null) {
            onDestroyCallback.call(this);
        }
    }

    @Override public void setOnDestroy(Action1<PausableSubscription> onDestroyCallback) {
        this.onDestroyCallback = onDestroyCallback;
    }
}
