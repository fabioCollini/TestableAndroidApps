package it.cosenonjaviste.testableandroidapps;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(overrides = true, library = true)
public class WelcomeDialogManagerTestModule {
    @Provides @Singleton
    public WelcomeDialogManager provideWelcomeDialogManager() {
        return new WelcomeDialogManager() {
            @Override public void showDialogIfNeeded() {
            }
        };
    }
}
