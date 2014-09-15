package it.cosenonjaviste.testableandroidapps.mvc;

import java.util.List;

import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.mvc.base.ContextBinder;
import it.cosenonjaviste.testableandroidapps.mvc.base.ObservableQueueItem;
import it.cosenonjaviste.testableandroidapps.mvc.base.RxMvcController;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by fabiocollini on 13/09/14.
 */
public class RepoListController extends RxMvcController<RepoListModel> {

    private RepoService repoService;

    public RepoListController(ContextBinder contextBinder, RepoService repoService) {
        super(contextBinder);
        this.repoService = repoService;
    }

    protected RepoListModel createModel() {
        return new RepoListModel();
    }

    public void listRepos(String queryString) {
        repoService.listRepos(contextBinder, queryString);
    }

    private Subscription subscribeRepoList() {
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

    private Subscription subscribeRepo() {
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

    public void toggleStar(Repo repo) {
        repoService.toggleStar(contextBinder, repo);
    }

    @Override protected CompositeSubscription initSubscriptions() {
        CompositeSubscription s = new CompositeSubscription();
        s.add(subscribeRepoList());
        s.add(subscribeRepo());
        return s;
    }
}
