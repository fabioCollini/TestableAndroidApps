package it.cosenonjaviste.testableandroidapps.mvc.base.pausable;

import rx.Observer;
import rx.functions.Action1;

/**
 * Created by fabiocollini on 05/10/14.
 */
public interface PausableSubscription {
    void pause();

    void resume();

    void resume(Observer<?> observer);

    void destroy();

    void setOnDestroy(Action1<PausableSubscription> callback);
}
