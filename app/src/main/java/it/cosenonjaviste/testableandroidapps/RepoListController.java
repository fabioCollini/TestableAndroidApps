package it.cosenonjaviste.testableandroidapps;

import android.support.v4.app.FragmentActivity;

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

    private RepoListModel model = new RepoListModel();

    public void listRepos(FragmentActivity activity, String queryString) {
        repoService.listRepos(activity, queryString);
    }

    public Subscription subscribeRepoList(final Action1<RepoListModel> refreshAction) {
        return repoService.getLoadQueue().subscribe(new Func1<ObservableQueueItem<List<Repo>>, Observable<RepoListModel>>() {
            @Override public Observable<RepoListModel> call(ObservableQueueItem<List<Repo>> item) {
                model.setProgressVisible(true);
                model.setReloadVisible(false);
                refreshAction.call(model);
                return item.getObservable().doOnTerminate(new Action0() {
                    @Override public void call() {
                        model.setProgressVisible(false);
                    }
                }).map(new Func1<List<Repo>, RepoListModel>() {
                    @Override public RepoListModel call(List<Repo> repos) {
                        model.setRepos(repos);
                        return model;
                    }
                }).doOnError(new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        model.setReloadVisible(true);
                    }
                });
            }
        }, terminateObserver(refreshAction));
    }

    private Observer<RepoListModel> terminateObserver(final Action1<RepoListModel> refreshAction) {
        return new Observer<RepoListModel>() {
            @Override public void onCompleted() {
                refreshAction.call(model);
            }

            @Override public void onError(Throwable e) {
                refreshAction.call(model);
            }

            @Override public void onNext(RepoListModel repoListModel) {
            }
        };
    }

    public Subscription subscribeRepo(final Action1<RepoListModel> refreshAction) {
        return repoService.getRepoQueue().subscribe(new Func1<ObservableQueueItem<Repo>, Observable<RepoListModel>>() {
            @Override public Observable<RepoListModel> call(final ObservableQueueItem<Repo> item) {
                model.getUpdatingRepos().add(item.getItem().getId());
                refreshAction.call(model);
                return item.getObservable().finallyDo(new Action0() {
                    @Override public void call() {
                        model.getUpdatingRepos().remove(item.getItem().getId());
                    }
                }).map(new Func1<Repo, RepoListModel>() {
                    @Override public RepoListModel call(Repo repo) {
                        return model;
                    }
                });
            }
        }, terminateObserver(refreshAction));
    }

    public void toggleStar(FragmentActivity activity, final Repo repo) {
        repoService.toggleStar(activity, repo);
    }

    public Subscription subscribe(final Action1<RepoListModel> action1) {
        CompositeSubscription subscription = new CompositeSubscription();
        subscription.add(subscribeRepoList(action1));
        subscription.add(subscribeRepo(action1));
        action1.call(model);
        return subscription;
    }
}
