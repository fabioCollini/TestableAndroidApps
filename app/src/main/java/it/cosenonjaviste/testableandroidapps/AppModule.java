package it.cosenonjaviste.testableandroidapps;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.cosenonjaviste.testableandroidapps.utils.Clock;
import it.cosenonjaviste.testableandroidapps.utils.ClockImpl;
import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaver;
import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaverImpl;

@Module(injects = MainActivity.class)
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
}
