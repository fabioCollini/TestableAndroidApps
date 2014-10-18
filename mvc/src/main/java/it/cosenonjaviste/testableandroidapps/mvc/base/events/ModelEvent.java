package it.cosenonjaviste.testableandroidapps.mvc.base.events;

public abstract class ModelEvent<M> {

    private M model;

    private Object extra;

    public ModelEvent(M model) {
        this.model = model;
    }

    public ModelEvent(M model, Object extra) {
        this.model = model;
        this.extra = extra;
    }

    public M getModel() {
        return model;
    }

    public Object getExtra() {
        return extra;
    }

    public boolean isExtraEmpty() {
        return extra == null;
    }
}
