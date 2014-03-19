package it.cosenonjaviste.testableandroidapps.share;

import android.content.Context;
import android.content.Intent;

public class ShareHelper {
    public static void share(Context context, String subject, String body) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        context.startActivity(Intent.createChooser(intent, "How do you want to share?"));
    }
}
