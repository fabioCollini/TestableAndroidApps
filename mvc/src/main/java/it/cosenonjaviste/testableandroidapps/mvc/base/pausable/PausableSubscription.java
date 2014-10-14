package it.cosenonjaviste.testableandroidapps.mvc.base.pausable;

import rx.Observer;
import rx.functions.Action0;

public interface PausableSubscription {
    void pause();

    void resume();

    void resume(Action0 onAttach, Observer<?> observer);

    void destroy();
}
