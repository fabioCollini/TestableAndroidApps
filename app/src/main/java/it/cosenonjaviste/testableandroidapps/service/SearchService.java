package it.cosenonjaviste.testableandroidapps.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import javax.inject.Inject;

import it.cosenonjaviste.testableandroidapps.base.ObjectGraphHolder;
import org.parceler.Parcels;

import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import retrofit.RestAdapter;

public class SearchService extends IntentService {

    public static final String EVENT_NAME = "search-event";
    public static final String QUERY = "query";
    public static final String REPOS = "repos";
    public static final String ERROR = "error";

    @Inject GitHubService service;

    public SearchService() {
        super("SearchService");
    }

    @Override public void onCreate() {
        super.onCreate();
        ObjectGraphHolder.inject(getApplication(), this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent resIntent = new Intent(EVENT_NAME);
        try {
            RepoResponse repos = service.listRepos(intent.getStringExtra(QUERY));
            resIntent.putExtra(REPOS, Parcels.wrap(repos));
        } catch (Throwable t) {
            resIntent.putExtra(ERROR, t.getMessage());
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(resIntent);
    }
}
