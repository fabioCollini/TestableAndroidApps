package it.cosenonjaviste.testableandroidapps.base;

import dagger.ObjectGraph;

public class ObjectGraphHolder {
    private static ObjectGraph objectGraph;

    private static ObjectGraphCreator objectGraphCreator;

    public static ObjectGraph getObjectGraph() {
        if (objectGraph == null) {
            objectGraph = objectGraphCreator.create();
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

    public static void inject(Object obj) {
        ObjectGraphHolder.getObjectGraph().inject(obj);
    }

}
