package it.cosenonjaviste.testableandroidapps.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;
import rx.observables.ConnectableObservable;
import rx.subscriptions.Subscriptions;

/**
 * Created by fabiocollini on 06/07/14.
 */
public class RxRetainedFragment<T> extends Fragment {

    private ConnectableObservable<T> observable;

    private Subscription subscription = UNSUBSCRIBED_SUBSCRIPTION;

    private Subscription connectableSubscription = Subscriptions.empty();

    public RxRetainedFragment() {
        setRetainInstance(true);
    }

    private static final Subscription UNSUBSCRIBED_SUBSCRIPTION = new Subscription() {
        @Override public void unsubscribe() {
        }

        @Override public boolean isUnsubscribed() {
            return true;
        }
    };

    private static <T> RxRetainedFragment<T> getFragment(FragmentActivity activity, String tag) {
        RxRetainedFragment<T> fragment = (RxRetainedFragment<T>) activity.getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new RxRetainedFragment<T>();
            activity.getSupportFragmentManager().beginTransaction().add(fragment, tag).commit();
        }
        return fragment;
    }

    private Subscription reconnectObservable(Observer<T> observer, Action0 onStart) {
        System.out.println("isUnsubscribed " + subscription.isUnsubscribed());
        if (observable != null && subscription.isUnsubscribed()) {
            onStart.call();
            subscription = observable.subscribe(observer);
            return subscription;
        }
        return UNSUBSCRIBED_SUBSCRIPTION;
    }

    private Subscription connectObservable(Observable<T> observable, Observer<T> observer, Action0 onStart) {
        this.observable = observable.replay(1);
        subscription.unsubscribe();
        onStart.call();
        subscription = this.observable.subscribe(observer);
        connectableSubscription = this.observable.connect();
        return subscription;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        connectableSubscription.unsubscribe();
    }

    public static <T> Subscription reconnectObservable(FragmentActivity activity, String tag, Observer<T> observer, Action0 onStart) {
        RxRetainedFragment<T> fragment = getFragment(activity, tag);
        return fragment.reconnectObservable(observer, onStart);
    }

    public static <T> Subscription connectObservable(FragmentActivity activity, String tag, Observable<T> observable, Observer<T> observer, Action0 onStart) {
        RxRetainedFragment<T> fragment = getFragment(activity, tag);
        return fragment.connectObservable(observable, observer, onStart);
    }
}
