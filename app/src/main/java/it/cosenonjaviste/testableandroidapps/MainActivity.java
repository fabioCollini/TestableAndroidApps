package it.cosenonjaviste.testableandroidapps;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.parceler.ParcelClass;
import org.parceler.ParcelClasses;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnItemClick;
import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphHolder;
import it.cosenonjaviste.testableandroidapps.base.RxMvcActivity;
import it.cosenonjaviste.testableandroidapps.model.Owner;
import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import it.cosenonjaviste.testableandroidapps.mvc.RepoListController;
import it.cosenonjaviste.testableandroidapps.mvc.RepoListModel;
import it.cosenonjaviste.testableandroidapps.mvc.RepoService;
import it.cosenonjaviste.testableandroidapps.mvc.base.Navigator;
import it.cosenonjaviste.testableandroidapps.share.ShareHelper;

@ParcelClasses({@ParcelClass(RepoResponse.class), @ParcelClass(Repo.class), @ParcelClass(Owner.class), @ParcelClass(RepoListModel.class)})
public class MainActivity extends RxMvcActivity<RepoListController, RepoListModel> {

    @InjectView(R.id.list) ListView listView;

    @InjectView(R.id.query) EditText query;

    @InjectView(R.id.progress) View progress;

    @InjectView(R.id.reload) View reload;

    @Inject WelcomeDialogManager welcomeDialogManager;

    @Inject Provider<RepoListController> presenterProvider;

    @Inject ShareHelper shareHelper;

    private RepoAdapter repoAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        ObjectGraph appObjectGraph = ObjectGraphHolder.getObjectGraph(getApplication());
        ObjectGraph activityObjectGraph = appObjectGraph.plus(new ActivityModule(this));
        activityObjectGraph.inject(this);

        super.onCreate(savedInstanceState);

        repoAdapter = new RepoAdapter(this);

        listView.setAdapter(repoAdapter);

        welcomeDialogManager.showDialogIfNeeded();
    }

    @Override protected Navigator getNavigator() {
        return null;
    }

    @OnItemClick(R.id.list) void shareItem(int position) {
        Repo repo = repoAdapter.getItem(position);
        presenter.toggleStar(repo);
//        shareHelper.share(repo.getName(), repo.getName() + " " + repo.getUrl());
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override protected Provider<RepoListController> getProvider() {
        return presenterProvider;
    }

    @Override public void updateView(RepoListModel model) {
        if (model.isProgressVisible()) {
            progress.setVisibility(View.VISIBLE);
            reload.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
        } else if (model.isReloadVisible()) {
            reload.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            reload.setVisibility(View.GONE);
        }
        repoAdapter.reloadData(model.getRepos(), model.getUpdatingRepos());
        if (!TextUtils.isEmpty(model.getExceptionMessage())) {
            Toast.makeText(MainActivity.this, model.getExceptionMessage(), Toast.LENGTH_LONG).show();
            model.setExceptionMessage(null);
        }
    }

    @OnEditorAction(R.id.query) boolean onSearch(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            executeSearch();
            return true;
        }
        return false;
    }

    @OnClick({R.id.search, R.id.reload}) void executeSearch() {
        String queryString = query.getText().toString();
        presenter.listRepos(queryString);
    }

    @Module(injects = MainActivity.class, addsTo = AppModule.class)
    public static class ActivityModule {
        private FragmentActivity activity;

        public ActivityModule(FragmentActivity activity) {
            this.activity = activity;
        }

        @Provides @Singleton public FragmentActivity provideActivity() {
            return activity;
        }

        @Provides @Singleton RepoListController provideController(RepoService repoService) {
            return new RepoListController(repoService);
        }
    }
}
