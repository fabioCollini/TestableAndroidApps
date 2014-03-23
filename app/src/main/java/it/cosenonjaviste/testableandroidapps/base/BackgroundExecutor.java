package it.cosenonjaviste.testableandroidapps.base;

import com.squareup.otto.Bus;

import java.lang.reflect.Constructor;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BackgroundExecutor {

    @Inject Bus eventBus;

    @Inject Executor executor;

    public <T> void executeInBackground(final T event, final Function<T, ?> action, final Class<?> errorClass) {
        executor.execute(new Runnable() {
            @Override public void run() {
                try {
                    Object result = action.apply(event);
                    if (result != null) {
                        eventBus.post(result);
                    }
                } catch (Throwable t) {
                    final Constructor<?> constructor;
                    try {
                        constructor = errorClass.getConstructor(Throwable.class);
                        eventBus.post(constructor.newInstance(t));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Error creating " + errorClass.getSimpleName() + "(" + Throwable.class.getSimpleName()
                                + ")", e);
                    }

                }
            }
        });
    }

    public <T> void executeInBackground(final T event, final Function<T, ?> action, final BiFunction<T, Throwable, ?> errorEventCreator) {
        executor.execute(new Runnable() {
            @Override public void run() {
                try {
                    Object result = action.apply(event);
                    if (result != null) {
                        eventBus.post(result);
                    }
                } catch (Throwable t) {
                    eventBus.post(errorEventCreator.apply(event, t));
                }
            }
        });
    }
}
