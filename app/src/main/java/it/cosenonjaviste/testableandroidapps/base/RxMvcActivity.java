package it.cosenonjaviste.testableandroidapps.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import javax.inject.Provider;

import butterknife.ButterKnife;
import it.cosenonjaviste.testableandroidapps.mvc.base.Navigator;
import it.cosenonjaviste.testableandroidapps.mvc.base.RxMvpPresenter;
import it.cosenonjaviste.testableandroidapps.mvc.base.RxMvpView;
import it.cosenonjaviste.testableandroidapps.mvc.base.pausable.PausableSubscription;

public abstract class RxMvcActivity<P extends RxMvpPresenter<M>, M> extends ActionBarActivity implements RxMvpView<M> {

    public static final String PRESENTER_ID = "presenterId";
    protected P presenter;

    private long presenterId;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initPresenter(savedInstanceState);

        BundleObjectSaver<M> objectSaver = new BundleObjectSaver<>(savedInstanceState, "model");
        BundlePresenterArgs args = new BundlePresenterArgs(getIntent().getExtras());
        presenter.init(new ActivityContextBinder(this), objectSaver, args, getNavigator());

        setContentView(getLayoutId());
        ButterKnife.inject(this);
        initView();
    }

    protected abstract Navigator getNavigator();

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.saveInBundle(new BundleObjectSaver<>(outState, "model"));
        outState.putLong(PRESENTER_ID, presenterId);
    }

    @Override public void onStart() {
        super.onStart();
        PausableSubscription subscription = presenter.subscribe(this);
        PausableSubscriptionsFragment.savePausableSubscriptions(getSupportFragmentManager(), subscription);
    }

    @Override public void onStop() {
        presenter.pause();
        super.onStop();
    }

    protected final P initPresenter(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            presenterId = savedInstanceState.getLong(PRESENTER_ID, 0);
            if (presenterId != 0) {
                presenter = PresenterSaverFragment.load(getSupportFragmentManager(), presenterId);
            }
        }
        if (presenter == null) {
            presenter = getProvider().get();
            presenterId = PresenterSaverFragment.save(getSupportFragmentManager(), presenter);
        }
        return presenter;
    }

    protected Provider<P> getProvider() {
        return null;
    }

    protected void initView() {
    }

    protected abstract int getLayoutId();
}
