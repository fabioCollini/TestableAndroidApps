package it.cosenonjaviste.testableandroidapps;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnItemClick;
import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import icepick.Icepick;
import it.cosenonjaviste.testableandroidapps.base.BackgroundExecutor;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphHolder;
import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import it.cosenonjaviste.testableandroidapps.share.ShareHelper;

public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.list) ListView listView;

    @InjectView(R.id.query) EditText query;

    @InjectView(R.id.progress) View progress;

    @InjectView(R.id.reload) View reload;

    @Inject WelcomeDialogManager welcomeDialogManager;

    @Inject EventBus eventBus;

    @Inject GitHubService service;

    @Inject BackgroundExecutor backgroundExecutor;

    private RepoAdapter repoAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ObjectGraph appObjectGraph = ObjectGraphHolder.getObjectGraph(getApplication());
        ObjectGraph activityObjectGraph = appObjectGraph.plus(new ActivityModule(this));
        activityObjectGraph.inject(this);

        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        repoAdapter = new RepoAdapter(this);
        Icepick.restoreInstanceState(repoAdapter, savedInstanceState);

        listView.setAdapter(repoAdapter);

        eventBus.register(this);

        welcomeDialogManager.showDialogIfNeeded();
    }

    public void onEventMainThread(RepoResponse event) {
        repoAdapter.reloadData(event.getItems());
        listView.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }

    public void onEventMainThread(SearchError event) {
        reload.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }

    @OnItemClick(R.id.list) void shareItem(int position) {
        Repo repo = repoAdapter.getItem(position);
        ShareHelper.share(this, repo.getName(), repo.getName() + " " + repo.getUrl());
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(repoAdapter, outState);
    }

    @Override protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    @OnEditorAction(R.id.query) boolean onSearch(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            executeSearch();
            return true;
        }
        return false;
    }

    @OnClick({R.id.search, R.id.reload}) void executeSearch() {
        progress.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

        backgroundExecutor.executeInBackground(query.getText().toString(),
                service::listRepos,
                SearchError::new);
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
    }
}
