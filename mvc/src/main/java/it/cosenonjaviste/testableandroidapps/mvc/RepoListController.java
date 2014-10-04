package it.cosenonjaviste.testableandroidapps.mvc;

import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.mvc.base.ContextBinder;
import it.cosenonjaviste.testableandroidapps.mvc.base.RxMvcController;
import rx.Subscription;
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
        return repoService.getLoadQueue().subscribe((item, observable) -> {
            model.setProgressVisible(true);
            model.setReloadVisible(false);
            notifyModelChanged();
            return observable.subscribe(model::setRepos, throwable -> {
                model.setProgressVisible(false);
                model.setReloadVisible(true);
                notifyModelChanged();
            }, () -> {
                model.setProgressVisible(false);
                notifyModelChanged();
            });
        });
    }

    private Subscription subscribeRepo() {
        return repoService.getRepoQueue().subscribe((item, observable) -> {
            model.getUpdatingRepos().add(item.getId());
            notifyModelChanged();
            return observable
                    .finallyDo(() -> model.getUpdatingRepos().remove(item.getId()))
                    .subscribe(repo -> {
                    }, e -> {
                        model.setExceptionMessage(e.getMessage());
                        notifyModelChanged();
                    }, this::notifyModelChanged);
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
