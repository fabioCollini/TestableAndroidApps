package it.cosenonjaviste.testableandroidapps.model;

import java.util.ArrayList;

public class RepoResponse {
    ArrayList<Repo> items;

    public RepoResponse() {
    }

    public RepoResponse(ArrayList<Repo> items) {
        this.items = items;
    }

    public ArrayList<Repo> getItems() {
        return items;
    }
}
