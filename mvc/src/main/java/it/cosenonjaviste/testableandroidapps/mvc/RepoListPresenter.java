package it.cosenonjaviste.testableandroidapps.mvc;

import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.model.RepoService;
import it.cosenonjaviste.testableandroidapps.mvc.base.PresenterArgs;
import it.cosenonjaviste.testableandroidapps.mvc.base.RxMvpPresenter;

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
                () -> publish(new ModelEvent<>(EventType.START_LOADING, model)),
                repos -> {
                    model.setReloadVisible(false);
                    model.setRepos(repos);
                    publish(new ModelEvent<>(EventType.END_LOADING, model));
                }, throwable -> {
                    model.setReloadVisible(true);
                    publish(new ModelEvent<>(EventType.ERROR, model, throwable));
                });
    }

    public void toggleStar(Repo repo) {
        subscribePausable(repoService.toggleStar(repo),
                () -> {
                    model.getUpdatingRepos().add(repo.getId());
                    publish(new ModelEvent<>(EventType.START_LOADING, model, repo));
                },
                repo1 -> {
                }, e -> {
                    model.getUpdatingRepos().remove(repo.getId());
                    publish(new ModelEvent<>(EventType.ERROR, model, repo, e));
                }, () -> {
                    model.getUpdatingRepos().remove(repo.getId());
                    publish(new ModelEvent<>(EventType.END_LOADING, model, repo));
                });
    }
}
