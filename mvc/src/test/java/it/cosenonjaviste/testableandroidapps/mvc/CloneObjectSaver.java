package it.cosenonjaviste.testableandroidapps.mvc;

import it.cosenonjaviste.testableandroidapps.mvc.base.ObjectSaver;

/**
* Created by fabiocollini on 14/09/14.
*/
public class CloneObjectSaver implements ObjectSaver<MultiListModel> {
    private MultiListModel model;

    @Override public void saveInBundle(MultiListModel model) {
        this.model = model.clona();
        System.out.println("Save " + this.model);
    }

    @Override public MultiListModel loadFromBundle() {
        System.out.println("Load " + model);
        return model;
    }

}
