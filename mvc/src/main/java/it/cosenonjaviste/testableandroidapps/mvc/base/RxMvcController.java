package it.cosenonjaviste.testableandroidapps.mvc.base;

import android.os.Bundle;

import org.parceler.Parcels;

import rx.Subscription;

/**
 * Created by fabiocollini on 14/09/14.
 */
public abstract class RxMvcController<M> {
    protected M model;

    protected RxMvcView<M> view;

    protected Subscription subscriptions;

    private String parcelableName;

    protected RxMvcController(String parcelableName) {
        this.parcelableName = parcelableName;
    }

    public void saveInBundle(Bundle outState) {
        outState.putParcelable(parcelableName, Parcels.wrap(model));
    }

    public void loadFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            model = Parcels.unwrap(savedInstanceState.getParcelable(parcelableName));
        }
        if (model == null) {
            model = createModel();
        }
    }

    protected abstract M createModel();

    public void unsubscribe() {
        view = null;
    }

    protected void notifyModelChanged() {
        if (view != null) {
            view.updateView(model);
        }
    }

    public void subscribe(final RxMvcView<M> view) {
        this.view = view;
        //TODO unsubscribe on activity destroy
        if (subscriptions == null) {
            subscriptions = initSubscriptions();
        }
        notifyModelChanged();
    }

    protected abstract Subscription initSubscriptions();
}
