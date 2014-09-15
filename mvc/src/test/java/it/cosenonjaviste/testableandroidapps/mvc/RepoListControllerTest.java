package it.cosenonjaviste.testableandroidapps.mvc;

import org.junit.Test;

import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import it.cosenonjaviste.testableandroidapps.mvc.base.RxMvcView;
import retrofit.http.Query;
import rx.Observable;

import static org.junit.Assert.assertNotNull;

public class RepoListControllerTest {
    @Test
    public void testLoad() {
        RepoListController controller = new RepoListController(new TestContextBinder(), new RepoService(new GitHubService() {
            @Override public Observable<RepoResponse> listReposRx(@Query("q") String query) {
                return Observable.just(TestUtils.fromJson("/response.json", RepoResponse.class));
            }
        }));

        controller.loadFromBundle(new EmptyObjectSaver());

        controller.subscribe(new RxMvcView<RepoListModel>() {
            @Override public void updateView(RepoListModel model) {
                assertNotNull(model.getRepos());
            }
        });

        controller.listRepos("aa");
    }
}