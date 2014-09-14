package it.cosenonjaviste.testableandroidapps;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import it.cosenonjaviste.testableandroidapps.model.Repo;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by fabiocollini on 13/09/14.
 */
public class RepoListController {

    @Inject RepoService repoService;

    private RepoListModel model;
    private Action1<RepoListModel> refreshAction;

    public void listRepos(FragmentActivity activity, String queryString) {
        repoService.listRepos(activity, queryString);
    }

    public Subscription subscribeRepoList() {
        return repoService.getLoadQueue().subscribe(
                new Action0() {
                    @Override public void call() {
                        model.setProgressVisible(true);
                        model.setReloadVisible(false);
                        notifyModelChanged();
                    }
                },
                new Observer<List<Repo>>() {
                    @Override public void onCompleted() {
                        model.setProgressVisible(false);
                        notifyModelChanged();
                    }

                    @Override public void onError(Throwable e) {
                        model.setProgressVisible(false);
                        model.setReloadVisible(true);
                        notifyModelChanged();
                    }

                    @Override public void onNext(List<Repo> repos) {
                        model.setRepos(repos);
                    }
                });
    }

    private void notifyModelChanged() {
        refreshAction.call(model);
    }

    public Subscription subscribeRepo() {
        return repoService.getRepoQueue().subscribe(new Func1<ObservableQueueItem<Repo>, Observable<Repo>>() {
            @Override public Observable<Repo> call(final ObservableQueueItem<Repo> item) {
                model.getUpdatingRepos().add(item.getItem().getId());
                notifyModelChanged();
                return item.getObservable().finallyDo(new Action0() {
                    @Override public void call() {
                        model.getUpdatingRepos().remove(item.getItem().getId());
                    }
                });
            }
        }, new Observer<Repo>() {
            @Override public void onCompleted() {
                notifyModelChanged();
            }

            @Override public void onError(Throwable e) {
                model.setExceptionMessage(e.getMessage());
                notifyModelChanged();
            }

            @Override public void onNext(Repo next) {
            }
        });
    }

    public void toggleStar(FragmentActivity activity, final Repo repo) {
        repoService.toggleStar(activity, repo);
    }

    public Subscription subscribe(final Action1<RepoListModel> refreshAction) {
        this.refreshAction = refreshAction;
        CompositeSubscription subscription = new CompositeSubscription();
        subscription.add(subscribeRepoList());
        subscription.add(subscribeRepo());
        notifyModelChanged();
        return subscription;
    }

    public void saveInBundle(Bundle outState) {
        outState.putParcelable("model", Parcels.wrap(model));
    }

    public void loadFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            model = Parcels.unwrap(savedInstanceState.getParcelable("model"));
        }
        if (model == null) {
            model = new RepoListModel();
        }
    }
}
