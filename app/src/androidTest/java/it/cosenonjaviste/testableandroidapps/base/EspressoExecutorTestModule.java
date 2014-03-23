package it.cosenonjaviste.testableandroidapps.base;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(overrides = true, library = true)
public class EspressoExecutorTestModule {
    private EspressoExecutor espressoExecutor;

    public EspressoExecutorTestModule(EspressoExecutor espressoExecutor) {
        this.espressoExecutor = espressoExecutor;
    }

    @Provides @Singleton
    public Executor provideEspressoExecutor() {
        return espressoExecutor;
    }
}
