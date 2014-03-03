package it.cosenonjaviste.testableandroidapps.base;

import android.app.Application;

import dagger.ObjectGraph;

public class ObjectGraphHolder {
    private static ObjectGraph objectGraph;

    private static ObjectGraphCreator objectGraphCreator;

    public static ObjectGraph getObjectGraph(Application app) {
        if (objectGraph == null) {
            objectGraph = objectGraphCreator.create(app);
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
