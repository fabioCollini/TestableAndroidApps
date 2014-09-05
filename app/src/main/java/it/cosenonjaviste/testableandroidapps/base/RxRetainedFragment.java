package it.cosenonjaviste.testableandroidapps.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;
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

    public static <T> RxRetainedFragment<T> getFragment(FragmentActivity activity, String tag) {
        RxRetainedFragment<T> fragment = (RxRetainedFragment<T>) activity.getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new RxRetainedFragment<T>();
            activity.getSupportFragmentManager().beginTransaction().add(fragment, tag).commit();
        }
        return fragment;
    }

    public Subscription reconnectObservable(Observer<T> observer) {
        if (observable != null && subscription.isUnsubscribed()) {
            subscription = observable.subscribe(observer);
            return subscription;
        }
        return UNSUBSCRIBED_SUBSCRIPTION;
    }

    public Subscription connectObservable(Observable<T> observable, Observer<T> observer) {
        this.observable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .replay(1);
        subscription.unsubscribe();
        subscription = this.observable.subscribe(observer);
        connectableSubscription = this.observable.connect();
        return subscription;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        connectableSubscription.unsubscribe();
    }

    public void destroy() {
        connectableSubscription.unsubscribe();
    }
}
