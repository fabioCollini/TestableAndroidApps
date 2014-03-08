package it.cosenonjaviste.testableandroidapps.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.event.EventBus;
import it.cosenonjaviste.testableandroidapps.EventBusRegistered;
import it.cosenonjaviste.testableandroidapps.SearchEvent;
import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;

@Singleton @EventBusRegistered
public class SearchService {

    @Inject GitHubService service;

    @Inject EventBus eventBus;

    protected void onEventBackgroundThread(SearchEvent event) {
        try {
            RepoResponse repos = service.listRepos(event.getQuery());
            eventBus.post(new SearchEvent.Result(repos.getItems()));
        } catch (Throwable t) {
            eventBus.post(new SearchEvent.Error());
        }
    }
}
