package it.cosenonjaviste.testableandroidapps.mvc;

import org.junit.Before;
import org.junit.Test;

import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import it.cosenonjaviste.testableandroidapps.model.RepoService;
import retrofit.http.Query;
import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RepoListControllerTest {

    private RepoListController controller;
    private RepoListModel model;

    @Before
    public void setup() {
        controller = new RepoListController(new RepoService(new GitHubService() {
            @Override public Observable<RepoResponse> listReposRx(@Query("q") String query) {
                return Observable.just(TestUtils.fromJson("/response.json", RepoResponse.class));
            }
        }));

        model = controller.init(new TestContextBinder(), new EmptyObjectSaver<>(), null, null);
        controller.subscribe(null);
    }

    @Test
    public void testLoad() {
        controller.listRepos("aa");

        assertNotNull(model.getRepos());

        controller.toggleStar(model.getRepos().get(1));

        assertEquals(true, model.getRepos().get(1).isStarred());
    }
}