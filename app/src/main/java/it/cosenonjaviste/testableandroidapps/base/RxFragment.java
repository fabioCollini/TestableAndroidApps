package it.cosenonjaviste.testableandroidapps.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import rx.Observable;
import rx.observables.ConnectableObservable;
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

    public static <T> ConnectableObservable<T> bindActivity(FragmentActivity activity, Observable<T> observable) {
        Observable<T> background = RxUtils.background(activity, observable);
        return bindObservable(activity.getSupportFragmentManager(), background);
    }

    public static <T> ConnectableObservable<T> bindObservable(FragmentManager fragmentManager, Observable<T> observable) {
        RxFragment fragment = (RxFragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new RxFragment();
            fragmentManager.beginTransaction().add(fragment, TAG).commit();
        }
        ConnectableObservable<T> replay = observable.replay(1);
        fragment.subscriptions.add(replay.connect());
        return replay;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }
}
