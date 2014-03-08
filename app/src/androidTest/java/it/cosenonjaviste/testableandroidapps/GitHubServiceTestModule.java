package it.cosenonjaviste.testableandroidapps;

import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.cosenonjaviste.testableandroidapps.model.GitHubService;

@Module(overrides = true, library = true)
public class GitHubServiceTestModule {
    private Resources resources;

    public GitHubServiceTestModule(Resources resources) {
        this.resources = resources;
    }

    @Provides @Singleton
    public GitHubService provideGitHubService() {
        return new GitHubServiceStub(resources, it.cosenonjaviste.testableandroidapps.test.R.raw.repos);
    }
}
