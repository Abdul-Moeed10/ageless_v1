package com.example.ageless_v1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Pills extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_reminder);

        createNotificationChannel();

        Button addReminderButton = findViewById(R.id.add_reminder_button);
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddReminderDialogue();
            }
        });

        populateReminderList();
    }

    private void showAddReminderDialogue() {
        LinearLayout dialogueLayout = new LinearLayout(Pills.this);
        dialogueLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText reminderNameEditText = new EditText(Pills.this);
        reminderNameEditText.setHint("Reminder Name");
        dialogueLayout.addView(reminderNameEditText);

        final TimePicker reminderTimePicker = new TimePicker(Pills.this);
        dialogueLayout.addView(reminderTimePicker);

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        final boolean[] selectedDays = new boolean[days.length];
        for (int i = 0; i < days.length; i++) {
            CheckBox dayCheckBox = new CheckBox(Pills.this);
            dayCheckBox.setText(days[i]);
            final int index = i;
            dayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    selectedDays[index] = isChecked;
                }
            });
            dialogueLayout.addView(dayCheckBox);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Pills.this);
        builder.setTitle("Add Reminder")
                .setView(dialogueLayout)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String reminderName = reminderNameEditText.getText().toString();
                        int hour = reminderTimePicker.getCurrentHour();
                        int minute = reminderTimePicker.getCurrentMinute();
                        String reminderTime = String.format("%02d:%02d", hour, minute);
                        List<String> reminderDays = new ArrayList<>();
                        for (int i = 0; i < selectedDays.length; i++) {
                            if (selectedDays[i]) {
                                reminderDays.add(days[i]);
                            }
                        }

                        Reminder reminder = new Reminder(reminderName, reminderTime, reminderDays);

                        insertReminder(reminder);

                        scheduleReminder(reminder);

                        populateReminderList();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void populateReminderList() {
        List<Reminder> reminders = getReminders();

        final LinearLayout reminderListLayout = findViewById(R.id.reminder_list_layout);

        reminderListLayout.removeAllViews();

        for (int i = 0; i < reminders.size(); i++) {
            final Reminder reminder = reminders.get(i);

            View reminderItemView = getLayoutInflater().inflate(R.layout.list_item_reminder, null);

            CheckBox reminderCheckbox = reminderItemView.findViewById(R.id.reminder_checkbox);
            TextView reminderNameTextView = reminderItemView.findViewById(R.id.reminder_name_textview);
            TextView reminderTimeTextView = reminderItemView.findViewById(R.id.reminder_time_textview);
            TextView reminderDaysTextView = reminderItemView.findViewById(R.id.reminder_days_textview);

            reminderNameTextView.setText(reminder.getName());
            reminderTimeTextView.setText(reminder.getTime());
            reminderDaysTextView.setText(TextUtils.join(", ", reminder.getDays()));

            reminderCheckbox.setTag(i);

            reminderListLayout.addView(reminderItemView);
        }

        Button deleteReminderButton = findViewById(R.id.delete_reminder_button);
        deleteReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < reminderListLayout.getChildCount(); i++) {
                    View reminderItemView = reminderListLayout.getChildAt(i);
                    CheckBox reminderCheckbox = reminderItemView.findViewById(R.id.reminder_checkbox);
                    if (reminderCheckbox.isChecked()) {
                        int index = (int)reminderCheckbox.getTag();
                        Reminder selectedReminder = reminders.get(index);

                        deleteReminder(selectedReminder);
                    }
                }

                populateReminderList();
            }
        });
    }

    private void insertReminder(Reminder reminder) {
        ReminderDatabaseHelper dbHelper = new ReminderDatabaseHelper(Pills.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", reminder.getName());
        values.put("time", reminder.getTime());
        values.put("days", TextUtils.join(",", reminder.getDays()));

        long id = db.insert("reminders", null, values);

        db.close();
    }

    private List<Reminder> getReminders() {
        List<Reminder> reminders = new ArrayList<>();

        ReminderDatabaseHelper dbHelper = new ReminderDatabaseHelper(Pills.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("reminders", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String daysString = cursor.getString(cursor.getColumnIndex("days"));
            List<String> days = Arrays.asList(daysString.split(","));
            Reminder reminder = new Reminder(name, time, days);
            reminders.add(reminder);
        }

        cursor.close();
        db.close();

        return reminders;
    }

    private void deleteReminder(Reminder reminder) {
        ReminderDatabaseHelper dbHelper = new ReminderDatabaseHelper(Pills.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = "name=? AND time=? AND days=?";
        String[] whereArgs = {reminder.getName(), reminder.getTime(), TextUtils.join(",", reminder.getDays())};
        db.delete("reminders", whereClause, whereArgs);

        db.close();
    }


    private void scheduleReminder(Reminder reminder) {
        String[] timeParts = reminder.getTime().split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(Pills.this, ReminderBroadcastReceiver.class);
        intent.putExtra(ReminderBroadcastReceiver.REMINDER_NAME_EXTRA, reminder.getName());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(Pills.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminder";
            String description = "Reminder notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("reminder_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}