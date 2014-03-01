package it.cosenonjaviste.testableandroidapps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import java.util.GregorianCalendar;

import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaver;

public class WelcomeDialog extends DialogFragment {

    public static void showDialogIfNeeded(FragmentActivity activity) {
        DatePrefsSaver datePrefsSaver = new DatePrefsSaver(activity, "welcome_dialog_last_date");
        if (isMorning() && !datePrefsSaver.isTodaySaved()) {
            new WelcomeDialog().show(activity.getSupportFragmentManager(), "welcome");
            datePrefsSaver.saveNow();
        }
    }

    public static boolean isMorning() {
        int hour = new GregorianCalendar().get(GregorianCalendar.HOUR_OF_DAY);
        return hour > 6 && hour < 12;
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.app_name).setMessage(R.string.welcome).create();
    }
}
