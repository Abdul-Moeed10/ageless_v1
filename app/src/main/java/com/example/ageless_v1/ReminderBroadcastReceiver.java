package com.example.ageless_v1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    public static final String REMINDER_NAME_EXTRA = "reminder_name";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the reminder name from the intent extras
        String reminderName = intent.getStringExtra(REMINDER_NAME_EXTRA);

        // Create a notification with the reminder name
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "reminder_channel")
                .setSmallIcon(R.drawable.logo_icon)
                .setContentTitle("Reminder")
                .setContentText(reminderName)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());
    }
}

