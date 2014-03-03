package it.cosenonjaviste.testableandroidapps;

import android.app.Application;

import dagger.ObjectGraph;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphCreator;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphHolder;

public class TestableApp extends Application {

    @Override public void onCreate() {
        super.onCreate();
        ObjectGraphHolder.setObjectGraphCreator(new ObjectGraphCreator() {
            @Override public ObjectGraph create(Application app) {
                return ObjectGraph.create(new AppModule(app));
            }
        });
    }
}
