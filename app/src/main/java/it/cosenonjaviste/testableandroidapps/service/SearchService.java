package it.cosenonjaviste.testableandroidapps.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import retrofit.RestAdapter;

public class SearchService extends IntentService {

    public static final String EVENT_NAME = "search-event";
    public static final String QUERY = "query";
    public static final String REPOS = "repos";
    public static final String ERROR = "error";

    public SearchService() {
        super("SearchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.github.com")
                .build();
        GitHubService service = restAdapter.create(GitHubService.class);

        Intent resIntent = new Intent(EVENT_NAME);
        try {
            RepoResponse repos = service.listRepos(intent.getStringExtra(QUERY));
            resIntent.putParcelableArrayListExtra(REPOS, repos.getItems());
        } catch (Throwable t) {
            resIntent.putExtra(ERROR, t.getMessage());
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(resIntent);
    }
}
