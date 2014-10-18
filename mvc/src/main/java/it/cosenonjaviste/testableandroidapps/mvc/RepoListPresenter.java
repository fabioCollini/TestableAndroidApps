package it.cosenonjaviste.testableandroidapps.mvc;

import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.model.RepoService;
import it.cosenonjaviste.testableandroidapps.mvc.base.PresenterArgs;
import it.cosenonjaviste.testableandroidapps.mvc.base.RxMvpPresenter;
import it.cosenonjaviste.testableandroidapps.mvc.base.events.EndLoadingModelEvent;
import it.cosenonjaviste.testableandroidapps.mvc.base.events.ErrorModelEvent;
import it.cosenonjaviste.testableandroidapps.mvc.base.events.StartLoadingModelEvent;

public class RepoListPresenter extends RxMvpPresenter<RepoListModel> {

    private RepoService repoService;

    public RepoListPresenter(RepoService repoService) {
        this.repoService = repoService;
    }

    protected RepoListModel createModel(PresenterArgs args) {
        return new RepoListModel();
    }

    public void listRepos(String queryString) {
        subscribePausable(repoService.listRepos(queryString),
                () -> publish(new StartLoadingModelEvent<>(model)),
                repos -> {
                    model.setReloadVisible(false);
                    model.setRepos(repos);
                    publish(new EndLoadingModelEvent<>(model));
                }, throwable -> {
                    model.setReloadVisible(true);
                    publish(new ErrorModelEvent<>(model, throwable));
                });
    }

    public void toggleStar(Repo repo) {
        subscribePausable(repoService.toggleStar(repo),
                () -> publish(new StartLoadingModelEvent<>(model, repo)),
                r -> publish(new EndLoadingModelEvent<>(model, r)),
                e -> publish(new ErrorModelEvent<>(model, repo, e))
        );
    }
}
