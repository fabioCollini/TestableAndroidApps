package it.cosenonjaviste.testableandroidapps.model;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface GitHubService {
    @GET("/search/repositories") RepoResponse listRepos(@Query("q") String query);

    @GET("/search/repositories") Observable<RepoResponse> listReposRx(@Query("q") String query);
}