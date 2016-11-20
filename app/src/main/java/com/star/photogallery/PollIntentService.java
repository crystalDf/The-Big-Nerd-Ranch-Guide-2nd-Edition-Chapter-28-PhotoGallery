package com.star.photogallery;


import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class PollIntentService extends IntentService {

    private static final String TAG = "PollIntentService";

    private static final long POLL_INTERVAL = 1000 * 60;

    public static Intent newIntent(Context context) {
        return new Intent(context, PollIntentService.class);
    }

    public PollIntentService() {
        super(TAG);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {

        Log.i(TAG, "set service alarm on(True)/off(False): " + isOn);

        Intent intent = PollIntentService.newIntent(context);

        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }

        QueryPreferences.setServiceOn(context, isOn);
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent intent = PollIntentService.newIntent(context);

        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent,
                PendingIntent.FLAG_NO_CREATE);

        return pendingIntent != null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        PollServiceUtils.pollFlickr(this);
    }

}
