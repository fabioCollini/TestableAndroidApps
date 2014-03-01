package it.cosenonjaviste.testableandroidapps;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = MainActivity.class)
public class AppModule {
    @Provides @Singleton
    public WelcomeDialogManager provideWelcomeDialogManager() {
        return new WelcomeDialogManager();
    }
}
