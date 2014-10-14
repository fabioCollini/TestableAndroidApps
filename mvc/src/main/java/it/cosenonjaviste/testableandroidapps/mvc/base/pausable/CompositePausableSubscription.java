package it.cosenonjaviste.testableandroidapps.mvc.base.pausable;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.functions.Action0;

public class CompositePausableSubscription implements PausableSubscription {

    private List<PausableSubscription> list = new ArrayList<PausableSubscription>();

    public void add(PausableSubscription pausableSubscription) {
        list.add(pausableSubscription);
    }

    @Override public void pause() {
        for (PausableSubscription subscription : list) {
            subscription.pause();
        }
    }

    @Override public void resume() {
        for (PausableSubscription subscription : list) {
            subscription.resume();
        }
    }

    @Override public void resume(Action0 onAttach, Observer<?> observer) {
        for (PausableSubscription subscription : list) {
            subscription.resume(onAttach, observer);
        }
    }

    @Override public void destroy() {
        for (PausableSubscription subscription : list) {
            subscription.destroy();
        }
    }
}
