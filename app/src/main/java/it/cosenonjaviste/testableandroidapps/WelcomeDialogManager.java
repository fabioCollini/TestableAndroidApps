package it.cosenonjaviste.testableandroidapps;

import android.support.v4.app.FragmentActivity;

import java.util.GregorianCalendar;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.cosenonjaviste.testableandroidapps.utils.Clock;
import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaver;

@Singleton
public class WelcomeDialogManager {

    @Inject Clock clock;

    @Inject DatePrefsSaver datePrefsSaver;

    public void showDialogIfNeeded(FragmentActivity activity) {
        if (isMorning() && !datePrefsSaver.isTodaySaved()) {
            new WelcomeDialog().show(activity.getSupportFragmentManager(), "welcome");
            datePrefsSaver.saveNow();
        }
    }

    public boolean isMorning() {
        int hour = clock.now().get(GregorianCalendar.HOUR_OF_DAY);
        return hour > 6 && hour < 12;
    }
}
