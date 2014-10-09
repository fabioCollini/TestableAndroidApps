package it.cosenonjaviste.testableandroidapps.mvc.base.pausable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by fabiocollini on 06/10/14.
 */
public class PausableSubscriptionMap<P> {
    private Map<String, List<Pair<P, PausableSubscription>>> pausableSubscriptions = new HashMap<>();

    public void save(String key, Pair<P, PausableSubscription> subscriptionPair) {
        List<Pair<P, PausableSubscription>> list = pausableSubscriptions.get(key);
        if (list == null) {
            list = new ArrayList<>();
            pausableSubscriptions.put(key, list);
        }
        list.add(subscriptionPair);
        subscriptionPair.second.setOnDestroy(this::remove);
    }

    private void remove(PausableSubscription pausableSubscription) {
        for (List<Pair<P, PausableSubscription>> pairs : pausableSubscriptions.values()) {
            for (Iterator<Pair<P, PausableSubscription>> iterator = pairs.iterator(); iterator.hasNext(); ) {
                Pair<P, PausableSubscription> next = iterator.next();
                if (next.second == pausableSubscription) {
                    iterator.remove();
                    return;
                }
            }
        }
    }

    public List<Pair<P, PausableSubscription>> get(String key) {
        return pausableSubscriptions.get(key);
    }

    public boolean isEmpty() {
        for (List<Pair<P, PausableSubscription>> pairs : pausableSubscriptions.values()) {
            if (!pairs.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
