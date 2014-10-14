package it.cosenonjaviste.testableandroidapps.mvc.base.pausable;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

abstract class PausableSubscriptionAdapter<T> implements PausableSubscription {
    private final Action0 onAttach;

    private final Observer<T> observer;

    protected Subscription mainSubscription;

    protected Subscription innerSubscription;

    protected int state;

    protected static final int RUNNING = 1;

    protected static final int PAUSED = 2;

    protected static final int TERMINATED = 3;

    PausableSubscriptionAdapter(Action0 onAttach, Observer<T> observer) {
        this.onAttach = onAttach;
        this.observer = observer;
    }

    protected Subscription subscribeInnerObservable(final Action0 onAttach, Observable<T> subject, Observer<? super T> observer) {
        return subject.doOnSubscribe(new Action0() {
            @Override public void call() {
                state = RUNNING;
                if (onAttach != null) {
                    onAttach.call();
                }
            }
        }).finallyDo(new Action0() {
            @Override public void call() {
                state = TERMINATED;
            }
        }).subscribe(observer);
    }

    @Override public void resume() {
        resume(onAttach, observer);
    }

    @Override public void destroy() {
        synchronized (this) {
            pause();
            if (state != TERMINATED) {
                mainSubscription.unsubscribe();
            }
        }
    }

    @Override public void pause() {
        if (state == RUNNING) {
            synchronized (this) {
                if (state == RUNNING) {
                    innerSubscription.unsubscribe();
                    innerSubscription = null;
                    state = PAUSED;
                }
            }
        }
    }
}
