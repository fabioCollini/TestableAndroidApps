package it.cosenonjaviste.testableandroidapps;

import android.support.v4.app.FragmentActivity;

import java.util.List;

import it.cosenonjaviste.testableandroidapps.base.ObservableQueue;
import it.cosenonjaviste.testableandroidapps.base.RxFragment;
import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
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
        Observable<List<Repo>> observable = gitHubService.listReposRx(queryString)
                .map(new Func1<RepoResponse, List<Repo>>() {
                    @Override public List<Repo> call(RepoResponse repoResponse) {
                        return repoResponse.getItems();
                    }
                });
        loadQueue.onNext(RxFragment.bindActivity(activity, observable));
    }

    public Subscription subscribeRepoList(final Action0 onStart, Action1<List<Repo>> onNext, Action1<Throwable> onError) {
        return loadQueue.subscribe(onStart, onNext, onError);
    }

    private int num;

    public Subscription subscribeRepo(Action0 onStart, Action1<Repo> onNext, Action1<Throwable> onError) {
        return repoQueue.subscribe(onStart, onNext, onError);
    }

    public void toggleStar(FragmentActivity activity, final Repo repo) {
        repo.setUpdating(true);
        Observable<Repo> observable = Observable
                .create(new Observable.OnSubscribe<Repo>() {
                    @Override public void call(Subscriber<? super Repo> subscriber) {
                        try {
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
                });
        repoQueue.onNext(RxFragment.bindActivity(activity, observable));
    }

}
