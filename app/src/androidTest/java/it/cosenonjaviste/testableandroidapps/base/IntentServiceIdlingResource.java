package it.cosenonjaviste.testableandroidapps.base;

import android.app.ActivityManager;
import android.content.Context;

import com.google.android.apps.common.testing.ui.espresso.IdlingResource;

public class IntentServiceIdlingResource implements IdlingResource {

    private final ActivityManager manager;

    private ResourceCallback resourceCallback;

    private boolean polling;
    private Class<?> serviceClass;

    public IntentServiceIdlingResource(Context context, Class<?> serviceClass) {
        this.serviceClass = serviceClass;
        manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Override public String getName() {
        return "IntentServiceIdlingResource";
    }

    @Override public boolean isIdleNow() {
        if (isServiceRunning()) {
            startPolling();
            return false;
        }
        return true;
    }

    private synchronized void startPolling() {
        if (!polling) {
            polling = true;
            new Thread() {
                @Override public void run() {
                    while (true) {
                        if (!isServiceRunning()) {
                            resourceCallback.onTransitionToIdle();
                            polling = false;
                            return;
                        }
                        try {
                            sleep(100);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }.start();
        }
    }

    private boolean isServiceRunning() {
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}
