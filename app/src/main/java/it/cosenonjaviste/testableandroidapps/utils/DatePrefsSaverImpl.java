package it.cosenonjaviste.testableandroidapps.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;

public class DatePrefsSaverImpl implements DatePrefsSaver {
    private String key;
    private final SharedPreferences prefs;

    public DatePrefsSaverImpl(Context context, String key) {
        this.key = key;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override public boolean isTodaySaved() {
        long savedValue = prefs.getLong(key, -1);
        if (savedValue == -1) {
            return false;
        }
        return DateUtils.isToday(savedValue);
    }

    @Override public void saveNow() {
        prefs.edit().putLong(key, System.currentTimeMillis()).commit();
    }
}
