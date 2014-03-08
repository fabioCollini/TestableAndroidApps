package it.cosenonjaviste.testableandroidapps;

import java.util.GregorianCalendar;

import it.cosenonjaviste.testableandroidapps.utils.Clock;

public class ClockStub implements Clock {
    private GregorianCalendar calendar;

    public ClockStub(int hour, int minute) {
        calendar = new GregorianCalendar();
        calendar.set(2014, 1, 1, hour, minute);
    }

    @Override public GregorianCalendar now() {
        return calendar;
    }
}
