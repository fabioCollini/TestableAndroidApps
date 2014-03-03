package it.cosenonjaviste.testableandroidapps.base;

import android.app.Application;

import dagger.ObjectGraph;

/**
 * Created by fabiocollini on 20/12/13.
 */
public interface ObjectGraphCreator {
    ObjectGraph create(Application app);
}
