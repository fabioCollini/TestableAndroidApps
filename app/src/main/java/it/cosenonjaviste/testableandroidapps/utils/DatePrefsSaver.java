package it.cosenonjaviste.testableandroidapps.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;

/**
 * Created by fabiocollini on 01/03/14.
 */
public class DatePrefsSaver {
    private String key;
    private final SharedPreferences prefs;

    public DatePrefsSaver(Context context, String key) {
        this.key = key;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isTodaySaved() {
        long savedValue = prefs.getLong(key, -1);
        if (savedValue == -1) {
            return false;
        }
        return DateUtils.isToday(savedValue);
    }

    public void saveNow() {
        prefs.edit().putLong(key, System.currentTimeMillis()).commit();
    }
}
