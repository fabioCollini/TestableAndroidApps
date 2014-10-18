package it.cosenonjaviste.testableandroidapps.mvc.base.events;

public class ErrorModelEvent<M> extends EndModelEvent<M> {

    private Throwable throwable;

    public ErrorModelEvent(M model, Throwable throwable) {
        super(model);
        this.throwable = throwable;
    }

    public ErrorModelEvent(M model, Object extra, Throwable throwable) {
        super(model, extra);
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
