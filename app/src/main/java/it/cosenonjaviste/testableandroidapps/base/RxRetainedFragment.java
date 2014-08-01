package it.cosenonjaviste.testableandroidapps.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by fabiocollini on 06/07/14.
 */
public class RxRetainedFragment<T> extends Fragment {

    private static final String TAG = "retainedFragmentTag";

    private Observable<T> observable;

    private Subscription subscription = Subscriptions.empty();

    private Subscription retainedSubscription = Subscriptions.empty();

    public RxRetainedFragment() {
        setRetainInstance(true);
    }

    public static <T> RxRetainedFragment<T> getFragment(FragmentActivity activity) {
        RxRetainedFragment<T> fragment = (RxRetainedFragment<T>) activity.getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new RxRetainedFragment<T>();
            activity.getSupportFragmentManager().beginTransaction().add(fragment, TAG).commit();
        }
        return fragment;
    }

    public boolean reconnectObservable(Observer<T> observer) {
        if (observable != null) {
            subscription = observable.subscribe(observer);
            return true;
        }
        return false;
    }

    public void connectObservable(Observable<T> observable, Observer<T> observer) {
        observable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .replay(1).refCount();
        this.observable = observable;
        subscription.unsubscribe();
        subscription = observable.subscribe(observer);
        retainedSubscription = observable.subscribe(new Observer<T>() {
            @Override public void onCompleted() {
                RxRetainedFragment.this.observable = null;
            }

            @Override public void onError(Throwable e) {
                RxRetainedFragment.this.observable = null;
            }

            @Override public void onNext(T t) {
            }
        });
    }

    @Override public void onDestroy() {
        super.onDestroy();
        retainedSubscription.unsubscribe();
    }

    public void unsubscribe(boolean keepObservable) {
        if (!keepObservable) {
            retainedSubscription.unsubscribe();
        }
        subscription.unsubscribe();
    }
}
