package it.cosenonjaviste.testableandroidapps.mvc;

import java.util.List;

import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import it.cosenonjaviste.testableandroidapps.mvc.base.ContextBinder;
import it.cosenonjaviste.testableandroidapps.mvc.base.ObservableQueue;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.util.async.Async;

/**
 * Created by fabiocollini on 03/07/14.
 */
public class RepoService {
    private GitHubService gitHubService;

    private ObservableQueue<Repo> repoQueue = new ObservableQueue<Repo>();

    private ObservableQueue<List<Repo>> loadQueue = new ObservableQueue<List<Repo>>();

    public RepoService(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    public void listRepos(ContextBinder contextBinder, String queryString) {
        Observable<List<Repo>> observable = gitHubService.listReposRx(queryString)
                .map(new Func1<RepoResponse, List<Repo>>() {
                    @Override public List<Repo> call(RepoResponse repoResponse) {
                        return repoResponse.getItems();
                    }
                });
        loadQueue.onNext(null, contextBinder.bindObservable(observable));
    }

    public ObservableQueue<List<Repo>> getLoadQueue() {
        return loadQueue;
    }

    private int num;

    public ObservableQueue<Repo> getRepoQueue() {
        return repoQueue;
    }

    public void toggleStar(ContextBinder contextBinder, final Repo repo) {
        Observable<Repo> observable = Async.start(new Func0<Repo>() {
            @Override public Repo call() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                num++;
                int aaa = 1 / (num % 3);
                repo.toggleStar();

                return repo;
            }
        });
        repoQueue.onNext(repo, contextBinder.bindObservable(observable));
    }

}
