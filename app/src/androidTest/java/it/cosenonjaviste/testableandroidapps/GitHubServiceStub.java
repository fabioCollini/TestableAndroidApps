package it.cosenonjaviste.testableandroidapps;

import android.content.res.Resources;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;

public class GitHubServiceStub implements GitHubService {

    private Resources resources;

    private int id;

    public GitHubServiceStub(Resources resources, int id) {
        this.resources = resources;
        this.id = id;
    }

    @Override public RepoResponse listRepos(String query) {
        StringBuilder b = new StringBuilder();
        InputStream resourceAsStream = resources.openRawResource(id);
        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                b.append(line);
            }
        } catch (IOException e) {
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
        return new Gson().fromJson(b.toString(), RepoResponse.class);
    }
}
