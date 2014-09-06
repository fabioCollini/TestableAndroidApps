package it.cosenonjaviste.testableandroidapps

import groovy.transform.CompileStatic
import it.cosenonjaviste.testableandroidapps.base.EndlessObserver
import it.cosenonjaviste.testableandroidapps.base.EndlessSubject
import it.cosenonjaviste.testableandroidapps.model.GitHubService
import it.cosenonjaviste.testableandroidapps.model.Repo
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by fabiocollini on 06/09/14.
 */
@CompileStatic
class RepoService {
    private GitHubService gitHubService;

    private EndlessSubject<Repo> subject = new EndlessSubject<Repo>();

    public RepoService(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    public rx.Observable<List<Repo>> listRepos(String queryString) {
        rx.Observable<List<Repo>> observable = gitHubService.listReposRx(queryString)
                .subscribeOn(Schedulers.io())
                .map({ (List<Repo>) it.getItems() });
        return observable;
    }

    private int num;

    public Subscription subscribe(EndlessObserver<Repo> observer) {
        return subject.subscribe(observer);
    }

    public void toggleStar(final Repo repo) {
        rx.Observable
                .create({ Subscriber<? super Repo> subscriber ->
            try {
                repo.setUpdating(true);
                subscriber.onNext(repo);
                Thread.sleep(2000);
                num++;
//                    int aaa = 1 / (num % 3);
                repo.toggleStar();
                subscriber.onNext(repo);
                subscriber.onCompleted();
            } catch (Throwable e) {
                subscriber.onError(e);
            }
        } as rx.Observable.OnSubscribe<Repo>)
                .finallyDo({ repo.setUpdating(false) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subject);
    }
}
