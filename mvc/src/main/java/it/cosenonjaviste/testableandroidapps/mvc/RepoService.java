package it.cosenonjaviste.testableandroidapps.mvc;

import java.util.List;

import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import rx.Observable;
import rx.Subscriber;

public class RepoService {
    private GitHubService gitHubService;

    public RepoService(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    public Observable<List<Repo>> listRepos(String queryString) {
        return gitHubService.listReposRx(queryString)
                .map(RepoResponse::getItems);
    }

    private int num;

    public Observable<Repo> toggleStar(final Repo repo) {
        return Observable
                .create((Subscriber<? super Repo> subscriber) -> {
                    try {
                        Thread.sleep(5000);
                        num++;
                        int aaa = 1 / (num % 3);
                        repo.toggleStar();
                        subscriber.onNext(repo);
                        subscriber.onCompleted();
                    } catch (Throwable e) {
                        subscriber.onError(e);
                    }
                });
    }

}
