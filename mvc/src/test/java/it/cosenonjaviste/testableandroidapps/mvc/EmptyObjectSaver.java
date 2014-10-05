package it.cosenonjaviste.testableandroidapps.mvc;

import it.cosenonjaviste.testableandroidapps.mvc.base.ObjectSaver;

/**
* Created by fabiocollini on 14/09/14.
*/
public class EmptyObjectSaver<M> implements ObjectSaver<M> {
    private M model;

    @Override public void saveInBundle(M model) {
        this.model = model;
    }

    @Override public M loadFromBundle() {
        return model;
    }
}
