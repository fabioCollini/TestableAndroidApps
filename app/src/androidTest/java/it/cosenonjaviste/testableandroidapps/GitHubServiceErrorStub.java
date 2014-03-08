package it.cosenonjaviste.testableandroidapps;

import android.content.res.Resources;

import it.cosenonjaviste.testableandroidapps.model.RepoResponse;

public class GitHubServiceErrorStub extends GitHubServiceStub {

    private boolean firstTime = true;

    public GitHubServiceErrorStub(Resources resources, int id) {
        super(resources, id);
    }

    @Override public RepoResponse listRepos(String query) {
        if (firstTime) {
            firstTime = false;
            throw new RuntimeException("Error");
        }
        return super.listRepos(query);
    }
}
