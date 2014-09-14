package it.cosenonjaviste.testableandroidapps.base;

import android.os.Bundle;

import org.parceler.Parcels;

import it.cosenonjaviste.testableandroidapps.mvc.base.ObjectSaver;

/**
 * Created by fabiocollini on 14/09/14.
 */
public class BundleObjectSaver<M> implements ObjectSaver<M> {
    private Bundle bundle;
    private String parcelableName;

    public BundleObjectSaver(Bundle bundle, String parcelableName) {
        this.bundle = bundle;
        this.parcelableName = parcelableName;
    }

    @Override public void saveInBundle(M model) {
        bundle.putParcelable(parcelableName, Parcels.wrap(model));
    }

    @Override public M loadFromBundle() {
        if (bundle != null) {
            return Parcels.unwrap(bundle.getParcelable(parcelableName));
        }
        return null;
    }
}
