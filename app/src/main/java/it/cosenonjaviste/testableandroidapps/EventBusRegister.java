package it.cosenonjaviste.testableandroidapps;

import java.util.Set;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class EventBusRegister {
    @Inject EventBus eventBus;

    @Inject @EventBusRegistered Set<Object> listeners;

    public void registerAll() {
        for (Object listener : listeners) {
            eventBus.register(listener);
        }
    }
}
