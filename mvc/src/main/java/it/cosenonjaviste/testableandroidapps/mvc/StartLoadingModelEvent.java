package it.cosenonjaviste.testableandroidapps.mvc;

public class StartLoadingModelEvent<M> extends ModelEvent<M> {
    public StartLoadingModelEvent(EventType type, M model) {
        super(type, model);
    }
}
