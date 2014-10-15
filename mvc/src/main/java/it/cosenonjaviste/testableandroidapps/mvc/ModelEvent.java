package it.cosenonjaviste.testableandroidapps.mvc;

public class ModelEvent<M> {

    private EventType type;

    private M model;

    private Object extra;

    private Throwable throwable;

    public ModelEvent(EventType type, M model) {
        this.type = type;
        this.model = model;
    }

    public ModelEvent(EventType type, M model, Object extra) {
        this.type = type;
        this.model = model;
        this.extra = extra;
    }

    public ModelEvent(EventType type, M model, Throwable throwable) {
        this.type = type;
        this.model = model;
        this.throwable = throwable;
    }

    public ModelEvent(EventType type, M model, Object extra, Throwable throwable) {
        this.type = type;
        this.model = model;
        this.extra = extra;
        this.throwable = throwable;
    }

    public EventType getType() {
        return type;
    }

    public M getModel() {
        return model;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public Object getExtra() {
        return extra;
    }

    public boolean isExtraEmpty() {
        return extra == null;
    }
}
