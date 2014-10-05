package it.cosenonjaviste.testableandroidapps.mvc.base;

/**
 * Created by fabiocollini on 05/10/14.
 */
public interface PausableSubscription<T> {
    void pause();

    void resume();

    void destroy();
}
