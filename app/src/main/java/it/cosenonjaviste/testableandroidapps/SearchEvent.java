package it.cosenonjaviste.testableandroidapps;

import java.util.ArrayList;

import it.cosenonjaviste.testableandroidapps.model.Repo;

public class SearchEvent {
    private String query;

    public SearchEvent(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public static class Result {
        private ArrayList<Repo> repos;

        public Result(ArrayList<Repo> repos) {
            this.repos = repos;
        }

        public ArrayList<Repo> getRepos() {
            return repos;
        }
    }

    public static class Error {

    }
}
