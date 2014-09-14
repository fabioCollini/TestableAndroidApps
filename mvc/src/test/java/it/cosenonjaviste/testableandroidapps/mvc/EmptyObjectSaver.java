package it.cosenonjaviste.testableandroidapps.mvc;

import it.cosenonjaviste.testableandroidapps.mvc.base.ObjectSaver;

/**
* Created by fabiocollini on 14/09/14.
*/
public class EmptyObjectSaver implements ObjectSaver<RepoListModel> {
    @Override public void saveInBundle(RepoListModel model) {
    }

    @Override public RepoListModel loadFromBundle() {
        return null;
    }
}
