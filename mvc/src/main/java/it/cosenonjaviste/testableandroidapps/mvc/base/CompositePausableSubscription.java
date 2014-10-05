package it.cosenonjaviste.testableandroidapps.mvc.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabiocollini on 05/10/14.
 */
public class CompositePausableSubscription implements PausableSubscription {

    private List<PausableSubscription> list = new ArrayList<>();

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

    @Override public void destroy() {
        for (PausableSubscription subscription : list) {
            subscription.destroy();
        }
    }
}
