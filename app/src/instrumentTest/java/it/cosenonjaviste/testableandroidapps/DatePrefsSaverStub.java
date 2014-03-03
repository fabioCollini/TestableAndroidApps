package it.cosenonjaviste.testableandroidapps;

import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaver;

public class DatePrefsSaverStub implements DatePrefsSaver {
    private boolean todaySaved;

    public DatePrefsSaverStub(boolean todaySaved) {
        this.todaySaved = todaySaved;
    }

    @Override public boolean isTodaySaved() {
        return todaySaved;
    }

    @Override public void saveNow() {
    }

}
