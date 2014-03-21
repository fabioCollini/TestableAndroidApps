package it.cosenonjaviste.testableandroidapps;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.cosenonjaviste.testableandroidapps.share.ShareHelper;

@Module(overrides = true, library = true)
public class ShareHelperTestModule {
    private ShareHelper shareHelper;

    public ShareHelperTestModule(ShareHelper shareHelper) {
        this.shareHelper = shareHelper;
    }

    @Provides @Singleton
    public ShareHelper provideShareHelper() {
        return shareHelper;
    }
}
