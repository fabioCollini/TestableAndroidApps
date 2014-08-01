package it.cosenonjaviste.testableandroidapps;

import java.util.List;

import javax.inject.Inject;

import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by fabiocollini on 03/07/14.
 */
public class RepoService {
    @Inject GitHubService gitHubService;

    public Observable<List<Repo>> listRepos(String queryString) {
        Observable<List<Repo>> observable = gitHubService.listReposRx(queryString)
                .subscribeOn(Schedulers.io())
                .map(new Func1<RepoResponse, List<Repo>>() {
                    @Override public List<Repo> call(RepoResponse repoResponse) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return repoResponse.getItems();
                    }
                });
        return observable;
    }

}
