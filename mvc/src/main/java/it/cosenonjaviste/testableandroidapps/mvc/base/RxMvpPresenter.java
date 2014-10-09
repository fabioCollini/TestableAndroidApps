package it.cosenonjaviste.testableandroidapps.mvc.base;


import it.cosenonjaviste.testableandroidapps.mvc.base.pausable.CompositePausableSubscription;
import it.cosenonjaviste.testableandroidapps.mvc.base.pausable.PausableSubscription;
import it.cosenonjaviste.testableandroidapps.mvc.base.pausable.PausableSubscriptions;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.observers.Observers;

public abstract class RxMvpPresenter<M> {
    protected M model;

    protected RxMvpView<M> view;

    protected CompositePausableSubscription pausableSubscriptions = new CompositePausableSubscription();

    protected ContextBinder contextBinder;

    private boolean newModelCreated;

    protected Navigator navigator;

    public void saveInBundle(ObjectSaver<M> objectSaver) {
        objectSaver.saveInBundle(model);
        newModelCreated = false;
    }

    public M init(ContextBinder contextBinder, ObjectSaver<M> objectSaver, PresenterArgs args, Navigator navigator) {
        this.contextBinder = contextBinder;
        this.navigator = navigator;
        model = objectSaver.loadFromBundle();
        if (model == null) {
            newModelCreated = true;
            model = createModel(args);
        }
        return model;
    }

    protected void loadOnFirstStart() {
    }

    protected abstract M createModel(PresenterArgs args);

    public void notifyModelChanged() {
        if (view != null) {
            view.updateView(model);
        }
    }

    public PausableSubscription subscribe(final RxMvpView<M> view) {
        this.view = view;
        if (pausableSubscriptions != null) {
            pausableSubscriptions.resume();
        }
        if (newModelCreated) {
            loadOnFirstStart();
        }
        notifyModelChanged();
        return pausableSubscriptions;
    }

    public void pause() {
        view = null;
        if (pausableSubscriptions != null) {
            pausableSubscriptions.pause();
        }
    }

    public void destroy() {
        if (pausableSubscriptions != null) {
            pausableSubscriptions.destroy();
        }
    }

    protected <T> void subscribePausable(Observable<T> observable, Observer<T> observer) {
        pausableSubscriptions.add(PausableSubscriptions.subscribe(contextBinder.bindObservable(observable), observer));
    }

    protected <T> void subscribePausable(Observable<T> observable, Action1<? super T> onNext, Action1<Throwable> onError) {
        pausableSubscriptions.add(PausableSubscriptions.subscribe(contextBinder.bindObservable(observable), Observers.create(onNext, onError)));
    }

    protected <T> void subscribePausable(Observable<T> observable, Action1<? super T> onNext, Action1<Throwable> onError, Scheduler scheduler) {
        if (scheduler != null) {
            observable = observable.subscribeOn(scheduler);
        }
        observable = contextBinder.bindObservable(observable);
        pausableSubscriptions.add(PausableSubscriptions.subscribe(observable, Observers.create(onNext, onError)));
    }

    protected <T> void subscribePausable(Observable<T> observable, Action1<? super T> onNext, Action1<Throwable> onError, Action0 onCompleted) {
        pausableSubscriptions.add(PausableSubscriptions.subscribe(contextBinder.bindObservable(observable), Observers.create(onNext, onError, onCompleted)));
    }
}
