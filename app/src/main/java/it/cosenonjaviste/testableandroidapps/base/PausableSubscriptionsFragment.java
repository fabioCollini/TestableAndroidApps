package it.cosenonjaviste.testableandroidapps.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import it.cosenonjaviste.testableandroidapps.mvc.base.pausable.CompositePausableSubscription;
import it.cosenonjaviste.testableandroidapps.mvc.base.pausable.PausableSubscription;

public class PausableSubscriptionsFragment extends Fragment {

    private static final String TAG = "PausableSubscriptionsFragment";
    private CompositePausableSubscription subscriptions = new CompositePausableSubscription();

    public PausableSubscriptionsFragment() {
        setRetainInstance(true);
    }

    public static void savePausableSubscriptions(FragmentManager fragmentManager, PausableSubscription subscription) {
        if (subscription != null) {
            PausableSubscriptionsFragment fragment = (PausableSubscriptionsFragment) fragmentManager.findFragmentByTag(TAG);
            if (fragment == null) {
                fragment = new PausableSubscriptionsFragment();
                fragmentManager.beginTransaction().add(fragment, TAG).commit();
            }
            fragment.subscriptions.add(subscription);
        }
    }

    @Override public void onDestroy() {
        super.onDestroy();
        subscriptions.destroy();
    }
}