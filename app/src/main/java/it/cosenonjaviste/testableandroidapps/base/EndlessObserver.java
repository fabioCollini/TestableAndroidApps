package it.cosenonjaviste.testableandroidapps.base;

/**
 * Created by fabiocollini on 02/08/14.
 */
public interface EndlessObserver<T> {
    void onNext(T t);

    void onNextError(Throwable t);
}
