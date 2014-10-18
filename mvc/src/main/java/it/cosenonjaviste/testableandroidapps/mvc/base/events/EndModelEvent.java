package it.cosenonjaviste.testableandroidapps.mvc.base.events;

public abstract class EndModelEvent<M> extends ModelEvent<M> {
    public EndModelEvent(M model) {
        super(model);
    }

    public EndModelEvent(M model, Object extra) {
        super(model, extra);
    }
}
