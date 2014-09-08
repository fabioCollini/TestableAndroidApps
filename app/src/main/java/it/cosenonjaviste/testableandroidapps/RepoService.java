package it.cosenonjaviste.testableandroidapps;

import android.support.v4.app.FragmentActivity;

import java.util.List;

import it.cosenonjaviste.testableandroidapps.base.ObservableQueue;
import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by fabiocollini on 03/07/14.
 */
public class RepoService {
    private GitHubService gitHubService;

    private ObservableQueue<Repo> repoQueue = new ObservableQueue<Repo>(false);

    private ObservableQueue<List<Repo>> loadQueue = new ObservableQueue<List<Repo>>(true);

    public RepoService(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    public void listRepos(FragmentActivity activity, String queryString) {
        loadQueue.start(activity, gitHubService.listReposRx(queryString)
                .map(new Func1<RepoResponse, List<Repo>>() {
                    @Override public List<Repo> call(RepoResponse repoResponse) {
                        return repoResponse.getItems();
                    }
                }));
    }

    public Observable<Observable<List<Repo>>> getRepoListObservable() {
        return loadQueue.getObservable();
    }

    private int num;

    public Observable<Observable<Repo>> getRepoObservable() {
        return repoQueue.getObservable();
    }

    public void toggleStar(FragmentActivity activity, final Repo repo) {
        repoQueue.start(activity,
                Observable
                        .create(new Observable.OnSubscribe<Repo>() {
                            @Override public void call(Subscriber<? super Repo> subscriber) {
                                try {
                                    repo.setUpdating(true);
                                    subscriber.onNext(repo);
                                    Thread.sleep(2000);
                                    num++;
                                    int aaa = 1 / (num % 3);
                                    repo.toggleStar();
                                    subscriber.onNext(repo);
                                    subscriber.onCompleted();
                                } catch (Throwable e) {
                                    subscriber.onError(e);
                                }
                            }
                        })
                        .finallyDo(new Action0() {
                            @Override public void call() {
                                repo.setUpdating(false);
                            }
                        }));
    }
}
