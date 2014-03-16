package it.cosenonjaviste.testableandroidapps;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import it.cosenonjaviste.testableandroidapps.model.GitHubService;
import it.cosenonjaviste.testableandroidapps.utils.Clock;
import it.cosenonjaviste.testableandroidapps.utils.ClockImpl;
import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaver;
import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaverImpl;
import retrofit.RestAdapter;

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
    public GitHubService provideGitHubService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.github.com")
                .build();
        return restAdapter.create(GitHubService.class);
    }

    @Provides @Singleton
    public EventBus provideEventBus() {
        return new EventBus();
    }
}
