package it.cosenonjaviste.testableandroidapps.mvc.base;

import rx.Subscription;

/**
 * Created by fabiocollini on 14/09/14.
 */
public abstract class RxMvcController<M> {
    protected M model;

    protected RxMvcView<M> view;

    protected Subscription subscriptions;

    protected ContextBinder contextBinder;

    protected RxMvcController(ContextBinder contextBinder) {
        this.contextBinder = contextBinder;
    }

    public void saveInBundle(ObjectSaver<M> objectSaver) {
        objectSaver.saveInBundle(model);
    }

    public void loadFromBundle(ObjectSaver<M> objectSaver) {
        model = objectSaver.loadFromBundle();
        if (model == null) {
            model = createModel();
        }
    }

    protected abstract M createModel();

    public void unsubscribeView() {
        view = null;
    }

    protected void notifyModelChanged() {
        if (view != null) {
            view.updateView(model);
        }
    }

    public void subscribe(final RxMvcView<M> view) {
        this.view = view;
        if (subscriptions == null) {
            subscriptions = initSubscriptions();
        }
        notifyModelChanged();
    }

    protected abstract Subscription initSubscriptions();

    public void destroy() {
        if (subscriptions != null) {
            subscriptions.unsubscribe();
            subscriptions = null;
        }
    }
}
