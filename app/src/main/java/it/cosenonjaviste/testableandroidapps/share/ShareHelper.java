package it.cosenonjaviste.testableandroidapps.share;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import javax.inject.Inject;

public class ShareHelper {

    @Inject FragmentActivity activity;

    public void share(String subject, String body) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        activity.startActivity(Intent.createChooser(intent, "How do you want to share?"));
    }
}
