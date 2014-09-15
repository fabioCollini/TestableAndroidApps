package it.cosenonjaviste.testableandroidapps.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import rx.Observable;
import rx.Observer;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by fabiocollini on 06/07/14.
 */
public class RxFragment extends Fragment {

    private static final String TAG = "RxFragment";
    private CompositeSubscription subscriptions = new CompositeSubscription();

    public RxFragment() {
        setRetainInstance(true);
    }

    public static <T> Observable<T> bindActivity(FragmentActivity activity, Observable<T> observable) {
        Observable<T> background = RxUtils.background(activity, observable);
        return bindObservable(activity.getSupportFragmentManager(), background);
    }

    public static <T> Observable<T> bindObservable(FragmentManager fragmentManager, Observable<T> observable) {
        RxFragment fragment = (RxFragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new RxFragment();
            fragmentManager.beginTransaction().add(fragment, TAG).commit();
        }
        Observable<T> refCount = observable.replay(1).refCount();
        fragment.subscriptions.add(refCount.subscribe(new Observer<T>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(T t) {

            }
        }));
        return refCount;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }
}
