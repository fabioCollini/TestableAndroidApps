package it.cosenonjaviste.testableandroidapps;

import android.app.Application;

import dagger.ObjectGraph;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphHolder;

public class TestableApp extends Application {

    @Override public void onCreate() {
        super.onCreate();
        ObjectGraphHolder.setObjectGraphCreator(app -> ObjectGraph.create(new AppModule(app)));
    }
}
