package it.cosenonjaviste.testableandroidapps;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.service.SearchService;
import it.cosenonjaviste.testableandroidapps.utils.Clock;
import it.cosenonjaviste.testableandroidapps.utils.ClockImpl;
import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaver;
import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaverImpl;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;

@Module(injects = {MainActivity.class, SearchService.class}, library = true)
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides @Singleton
    public Clock provideClock() {
        return new ClockImpl();
    }

    @Provides @Singleton
    public DatePrefsSaver provideDatePrefsSaver() {
        return new DatePrefsSaverImpl(application, "welcome_dialog_last_date");
    }

    @Provides @Singleton
    public Client provideOkHttpClient() {
        return new OkClient(new OkHttpClient());
    }

    @Provides @Singleton
    public GitHubService provideGitHubService(Client client) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.github.com")
                .setClient(client)
                .build();
        return restAdapter.create(GitHubService.class);
    }
}
