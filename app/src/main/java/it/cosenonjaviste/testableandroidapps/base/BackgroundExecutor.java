package it.cosenonjaviste.testableandroidapps.base;

import java.lang.reflect.Constructor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.event.EventBus;

@Singleton
public class BackgroundExecutor {

    @Inject EventBus eventBus;

    private ExecutorService executor = Executors.newCachedThreadPool();

    public <T> void executeInBackground(final T event, final Class<?> errorClass, final Function<T, ?> action) {
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
}
