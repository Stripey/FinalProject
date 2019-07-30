package edu.fsu.cs.preppy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("Tagging", "Alarm.class Alarmming");

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        CharSequence contentTitle = "Preppy: Meal Prep Reminder";
        CharSequence contentText = "Reminder to log your meal prep for this week.";
        Intent notificationIntent = new Intent(context,
                MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(
                context);
        builder.setContentTitle(contentTitle);
        builder.setContentText(contentText);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("Hi there");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);

        nm.notify(1, builder.build());

        Toast.makeText(context, "Alarm ...", Toast.LENGTH_LONG).show();
    }
}
