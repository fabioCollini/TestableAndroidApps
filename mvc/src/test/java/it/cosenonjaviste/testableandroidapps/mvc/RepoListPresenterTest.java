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

public class RepoListPresenterTest {

    private RepoListPresenter presenter;
    private RepoListModel model;

    @Before
    public void setup() {
        presenter = new RepoListPresenter(new RepoService(new GitHubService() {
            @Override public Observable<RepoResponse> listReposRx(@Query("q") String query) {
                return Observable.just(TestUtils.fromJson("/response.json", RepoResponse.class));
            }
        }));

        model = presenter.init(new TestContextBinder(), new EmptyObjectSaver<>(), null, null);
        presenter.subscribe(null);
    }

    @Test
    public void testLoad() {
        presenter.listRepos("aa");

        assertNotNull(model.getRepos());

        presenter.toggleStar(model.getRepos().get(1));

        assertEquals(true, model.getRepos().get(1).isStarred());
    }
}