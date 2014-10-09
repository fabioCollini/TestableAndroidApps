package it.cosenonjaviste.testableandroidapps;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.model.RepoService;
import it.cosenonjaviste.testableandroidapps.utils.Clock;
import it.cosenonjaviste.testableandroidapps.utils.ClockImpl;
import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaver;
import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaverImpl;
import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;
import retrofit.client.Client;
import retrofit.client.OkClient;

@Module(injects = {}, library = true)
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
                        //http calls are executed in background thread using RxUtils
                .setExecutors(Runnable::run, new MainThreadExecutor())
                .build();
        if (BuildConfig.DEBUG) {
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        return restAdapter.create(GitHubService.class);
    }

    @Provides @Singleton
    public RepoService provideRepoService(GitHubService gitHubService) {
        return new RepoService(gitHubService);
    }
}
