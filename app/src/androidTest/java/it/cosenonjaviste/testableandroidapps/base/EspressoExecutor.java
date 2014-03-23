package it.cosenonjaviste.testableandroidapps.base;

import com.google.android.apps.common.testing.ui.espresso.IdlingResource;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EspressoExecutor extends ThreadPoolExecutor implements IdlingResource {

    private int runningTasks;
    private ResourceCallback resourceCallback;

    private static EspressoExecutor singleton;

    public EspressoExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public static EspressoExecutor newCachedThreadPool() {
        if (singleton == null) {
            singleton = new EspressoExecutor(0, Integer.MAX_VALUE,
                    60L, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>());
        }
        return singleton;
    }

    @Override public void execute(final Runnable command) {
        super.execute(new Runnable() {
            @Override public void run() {
                try {
                    incrementRunningTasks();
                    command.run();
                } finally {
                    decrementRunningTasks();
                }
            }
        });
    }

    private synchronized void decrementRunningTasks() {
        runningTasks--;
        if (runningTasks == 0 && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
    }

    private synchronized void incrementRunningTasks() {
        runningTasks++;
    }

    @Override public String getName() {
        return "EspressoExecutor";
    }

    @Override public boolean isIdleNow() {
        return runningTasks == 0;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}
