package it.cosenonjaviste.testableandroidapps;

import java.util.ArrayList;

import it.cosenonjaviste.testableandroidapps.model.Repo;

public class SearchResult {
    private ArrayList<Repo> repos;

    public SearchResult(ArrayList<Repo> repos) {
        this.repos = repos;
    }

    public ArrayList<Repo> getRepos() {
        return repos;
    }
}
