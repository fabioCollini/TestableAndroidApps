package it.cosenonjaviste.testableandroidapps.base;

import android.os.Bundle;

import org.parceler.Parcels;

import it.cosenonjaviste.testableandroidapps.mvc.base.PresenterArgs;

public class BundlePresenterArgs implements PresenterArgs {
    private Bundle args;

    public BundlePresenterArgs() {
        this(new Bundle());
    }

    public BundlePresenterArgs(Bundle args) {
        this.args = args != null ? args : new Bundle();
    }

    @Override public <T> T getObject(String key) {
        return Parcels.unwrap(args.getParcelable(key));
    }

    @Override public PresenterArgs putObject(String key, Object value) {
        args.putParcelable(key, Parcels.wrap(value));
        return this;
    }

    public Bundle getBundle() {
        return args;
    }
}
