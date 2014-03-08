package it.cosenonjaviste.testableandroidapps.base;

import android.app.Application;

import dagger.ObjectGraph;
import it.cosenonjaviste.testableandroidapps.EventBusRegister;

public class ObjectGraphHolder {
    private static ObjectGraph objectGraph;

    private static ObjectGraphCreator objectGraphCreator;

    public static ObjectGraph getObjectGraph(Application app) {
        if (objectGraph == null) {
            objectGraph = objectGraphCreator.create(app);
            EventBusRegister eventBusRegister = objectGraph.get(EventBusRegister.class);
            eventBusRegister.registerAll();
        }
        return objectGraph;
    }

    public static void setObjectGraphCreator(ObjectGraphCreator objectGraphCreator) {
        if (ObjectGraphHolder.objectGraphCreator == null) {
            ObjectGraphHolder.objectGraphCreator = objectGraphCreator;
        }
    }

    public static void forceObjectGraphCreator(ObjectGraphCreator objectGraphCreator) {
        ObjectGraphHolder.objectGraphCreator = objectGraphCreator;
        objectGraph = null;
    }

    public static void inject(Application app, Object obj) {
        ObjectGraphHolder.getObjectGraph(app).inject(obj);
    }

}
