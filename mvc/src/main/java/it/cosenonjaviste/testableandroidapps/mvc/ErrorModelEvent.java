package it.cosenonjaviste.testableandroidapps.mvc;

public class ErrorModelEvent<M> extends ModelEvent<M> {

    private Throwable throwable;

    public ErrorModelEvent(EventType type, M model, Throwable throwable) {
        super(type, model);
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
