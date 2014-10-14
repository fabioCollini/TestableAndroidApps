package it.cosenonjaviste.testableandroidapps.mvc.base;

/**
 * Created by fabiocollini on 14/09/14.
 */
public interface RxMvpView<M> {
    void updateView(M model);

    void showProgress(Object obj);
}
