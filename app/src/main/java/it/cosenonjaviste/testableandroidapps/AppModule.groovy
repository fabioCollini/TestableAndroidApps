package it.cosenonjaviste.testableandroidapps

import android.app.Application
import com.squareup.okhttp.OkHttpClient
import dagger.Module
import dagger.Provides
import groovy.transform.CompileStatic
import it.cosenonjaviste.testableandroidapps.model.GitHubService
import it.cosenonjaviste.testableandroidapps.utils.Clock
import it.cosenonjaviste.testableandroidapps.utils.ClockImpl
import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaver
import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaverImpl
import retrofit.RestAdapter
import retrofit.client.Client
import retrofit.client.OkClient

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by fabiocollini on 06/09/14.
 */
@CompileStatic
@Module(library = true)
class AppModule {
    private Application application

    AppModule(Application application) {
        this.application = application
    }

    @Provides
    @javax.inject.Singleton
    Clock provideClock() {
        new ClockImpl()
    }

    @Provides
    @javax.inject.Singleton
    DatePrefsSaver provideDatePrefsSaver() {
        new DatePrefsSaverImpl(application, "welcome_dialog_last_date")
    }

    @Provides
    @javax.inject.Singleton
    Client provideOkHttpClient() {
        new OkClient(new OkHttpClient())
    }

    @Provides
    @javax.inject.Singleton
    GitHubService provideGitHubService(Client client) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.github.com")
                .setClient(client)
                .build()
        if (BuildConfig.DEBUG) {
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL)
        }
        restAdapter.create(GitHubService.class)
    }

    @Provides
    @javax.inject.Singleton
    Executor provideExecutor() {
        Executors.newCachedThreadPool()
    }

    @Provides
    @javax.inject.Singleton
    RepoService provideRepoService(GitHubService gitHubService) {
        new RepoService(gitHubService)
    }
}
