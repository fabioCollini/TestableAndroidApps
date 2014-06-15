package it.cosenonjaviste.testableandroidapps;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.client.Client;

@Module(overrides = true, library = true)
public class ClientErrorStubTestModule {
    private Resources resources;

    public ClientErrorStubTestModule(Resources resources) {
        this.resources = resources;
    }

    @Provides @Singleton
    public Client provideGitHubService() {
        Map<String, int[]> urlMapping = new HashMap<String, int[]>();
        urlMapping.put("https://api.github.com/search/repositories\\?q=.*", new int[]{it.cosenonjaviste.testableandroidapps.test.R.raw.repos});
        return new ClientErrorStub(resources, urlMapping);
    }
}
