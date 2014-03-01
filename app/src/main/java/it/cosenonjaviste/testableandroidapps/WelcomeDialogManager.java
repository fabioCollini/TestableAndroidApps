package it.cosenonjaviste.testableandroidapps;

import android.support.v4.app.FragmentActivity;

import java.util.GregorianCalendar;

import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaver;

public class WelcomeDialogManager {
    public void showDialogIfNeeded(FragmentActivity activity) {
        DatePrefsSaver datePrefsSaver = new DatePrefsSaver(activity, "welcome_dialog_last_date");
        if (isMorning() && !datePrefsSaver.isTodaySaved()) {
            new WelcomeDialog().show(activity.getSupportFragmentManager(), "welcome");
            datePrefsSaver.saveNow();
        }
    }

    public boolean isMorning() {
        int hour = new GregorianCalendar().get(GregorianCalendar.HOUR_OF_DAY);
        return hour > 6 && hour < 12;
    }
}
