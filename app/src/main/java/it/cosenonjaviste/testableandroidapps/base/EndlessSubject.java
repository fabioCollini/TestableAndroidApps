package it.cosenonjaviste.testableandroidapps.base;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by fabiocollini on 02/08/14.
 */
public class EndlessSubject<T> implements Observer<T> {

    private PublishSubject<Result<T>> subject = PublishSubject.create();

    @Override public void onCompleted() {
    }

    @Override public void onError(Throwable e) {
        subject.onNext(new Result<T>(null, e));
    }

    @Override public void onNext(T repo) {
        subject.onNext(new Result<T>(repo, null));
    }

    public Subscription subscribe(final EndlessObserver<T> endlessObserver) {
        return subject.subscribe(new Action1<Result<T>>() {
            @Override public void call(Result<T> result) {
                if (result.throwable != null) {
                    endlessObserver.onNextError(result.throwable);
                } else {
                    endlessObserver.onNext(result.success);
                }
            }
        });
    }

    private static class Result<T> {
        T success;
        Throwable throwable;

        public Result(T success, Throwable throwable) {
            this.success = success;
            this.throwable = throwable;
        }
    }
}
