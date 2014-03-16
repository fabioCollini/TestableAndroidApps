package it.cosenonjaviste.testableandroidapps;

public class SearchError {
    private Throwable error;

    public SearchError(Throwable error) {
        this.error = error;
    }

    public Throwable getError() {
        return error;
    }
}
