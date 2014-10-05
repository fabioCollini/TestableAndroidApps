package it.cosenonjaviste.testableandroidapps.base;

import android.support.v7.app.ActionBarActivity;

import it.cosenonjaviste.testableandroidapps.mvc.base.RxMvcController;
import it.cosenonjaviste.testableandroidapps.mvc.base.RxMvcView;

/**
 * Created by fabiocollini on 14/09/14.
 */
public abstract class RxMvcActivity<M> extends ActionBarActivity implements RxMvcView<M> {

    @Override protected void onStart() {
        super.onStart();
        getController().subscribe(this);
    }

    @Override protected void onStop() {
        getController().unsubscribeView();
        super.onStop();
    }

    @Override protected void onDestroy() {
        getController().destroy();
        super.onDestroy();
    }

    protected abstract RxMvcController<M> getController();
}
