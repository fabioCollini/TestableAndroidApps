package it.cosenonjaviste.testableandroidapps;

import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.cosenonjaviste.testableandroidapps.model.GitHubService;

@Module(overrides = true, library = true)
public class GitHubServiceErrorTestModule {
    private Resources resources;

    public GitHubServiceErrorTestModule(Resources resources) {
        this.resources = resources;
    }

    @Provides @Singleton
    public GitHubService provideGitHubService() {
        return new GitHubServiceErrorStub(resources, it.cosenonjaviste.testableandroidapps.test.R.raw.repos);
    }
}
