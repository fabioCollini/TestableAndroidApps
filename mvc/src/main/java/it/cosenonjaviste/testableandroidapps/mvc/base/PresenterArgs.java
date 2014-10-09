package it.cosenonjaviste.testableandroidapps.mvc.base;

public interface PresenterArgs {
    <T> T getObject(String key);

    PresenterArgs putObject(String key, Object value);
}
