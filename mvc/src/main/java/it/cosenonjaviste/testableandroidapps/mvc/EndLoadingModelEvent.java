package it.cosenonjaviste.testableandroidapps.mvc;

public class EndLoadingModelEvent<M> extends ModelEvent<M> {
    public EndLoadingModelEvent(EventType type, M model) {
        super(type, model);
    }
}
