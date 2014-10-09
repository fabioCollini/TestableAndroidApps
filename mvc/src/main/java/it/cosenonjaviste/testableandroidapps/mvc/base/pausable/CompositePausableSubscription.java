package it.cosenonjaviste.testableandroidapps.mvc.base.pausable;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by fabiocollini on 05/10/14.
 */
public class CompositePausableSubscription implements PausableSubscription {

    private List<PausableSubscription> list = new ArrayList<>();

    public void add(PausableSubscription pausableSubscription) {
        list.add(pausableSubscription);
    }

    public void add(Subscription subscription) {
        list.add(new PausableSubscription() {
            @Override public void pause() {
                subscription.unsubscribe();
            }

            @Override public void resume(Observer<?> observer) {
            }

            @Override public void resume() {
            }

            @Override public void destroy() {
            }

            @Override public void setOnDestroy(Action1<PausableSubscription> callback) {
            }
        });
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

    @Override public void resume(Observer<?> observer) {
        for (PausableSubscription subscription : list) {
            subscription.resume(observer);
        }
    }

    @Override public void destroy() {
        for (PausableSubscription subscription : list) {
            subscription.destroy();
        }
    }

    @Override public void setOnDestroy(Action1<PausableSubscription> callback) {
        for (PausableSubscription subscription : list) {
            subscription.setOnDestroy(callback);
        }
    }
}
