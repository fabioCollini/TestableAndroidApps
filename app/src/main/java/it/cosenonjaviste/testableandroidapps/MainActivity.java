package it.cosenonjaviste.testableandroidapps;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

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
import icepick.Icepick;
import it.cosenonjaviste.testableandroidapps.base.BackgroundExecutor;
import it.cosenonjaviste.testableandroidapps.base.BiFunction;
import it.cosenonjaviste.testableandroidapps.base.Function;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphHolder;
import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.share.ShareHelper;

public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.list) ListView listView;

    @InjectView(R.id.query) EditText query;

    @InjectView(R.id.progress) View progress;

    @InjectView(R.id.reload) View reload;

    @Inject WelcomeDialogManager welcomeDialogManager;

    @Inject Bus eventBus;

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

    @Subscribe public void reloadData(SearchResult event) {
        repoAdapter.reloadData(event.getRepos());
        listView.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }

    @Subscribe public void onSearchError(SearchError event) {
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

        String queryString = query.getText().toString();
        backgroundExecutor.executeInBackground(queryString, new Function<String, SearchResult>() {
            @Override public SearchResult apply(String query) {
                return new SearchResult(service.listRepos(query).getItems());
            }
        }, new BiFunction<String, Throwable, Object>() {
            @Override public Object apply(String s, Throwable throwable) {
                return new SearchError(throwable);
            }
        });
//        backgroundExecutor.executeInBackground(queryString, new Function<String, SearchResult>() {
//            @Override public SearchResult apply(String query) {
//                return new SearchResult(service.listRepos(query).getItems());
//            }
//        }, SearchError.class);
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
