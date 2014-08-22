package it.cosenonjaviste.testableandroidapps;

import java.util.List;

import it.cosenonjaviste.testableandroidapps.base.EndlessObserver;
import it.cosenonjaviste.testableandroidapps.base.EndlessSubject;
import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by fabiocollini on 03/07/14.
 */
public class RepoService {
    private GitHubService gitHubService;

    private EndlessSubject<Repo> subject = new EndlessSubject<Repo>();

    public RepoService(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    public Observable<List<Repo>> listRepos(String queryString) {
        Observable<List<Repo>> observable = gitHubService.listReposRx(queryString)
                .subscribeOn(Schedulers.io())
                .map(new Func1<RepoResponse, List<Repo>>() {
                    @Override public List<Repo> call(RepoResponse repoResponse) {
                        return repoResponse.getItems();
                    }
                });
        return observable;
    }

    private int num;

    public Subscription subscribe(EndlessObserver<Repo> observer) {
        return subject.subscribe(observer);
    }

    public void toggleStar(final Repo repo) {
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
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subject);
    }
}
