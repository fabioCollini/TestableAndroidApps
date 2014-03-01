package it.cosenonjaviste.testableandroidapps;

import android.app.Application;

import dagger.ObjectGraph;

public class TestableApp extends Application {

    private ObjectGraph objectGraph;

    @Override public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(new AppModule());
    }

    public void inject(Object obj) {
        objectGraph.inject(obj);
    }
}
