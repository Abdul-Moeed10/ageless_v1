package com.example.ageless_v1;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class To_do extends AppCompatActivity {

    private ListView taskListView;
    private List<Task> taskList;
    private TaskAdapter taskListAdapter;
    private TaskDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        taskListView = findViewById(R.id.taskList);
        taskList = new ArrayList<>();
        databaseHelper = new TaskDatabaseHelper(this);

        ImageButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog();
            }
        });

        ImageButton deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCheckedTasks();
            }
        });

        loadTasks();

        taskListAdapter = new TaskAdapter(this, taskList);
        taskListView.setAdapter(taskListAdapter);
    }

    // Method to create the task dialogue
    private void showAddTaskDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_task, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextTask = dialogView.findViewById(R.id.editTextTask);

        dialogBuilder.setTitle("Add Task");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String task = editTextTask.getText().toString().trim();
                if (!task.isEmpty()) {
                    addTask(task);
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    // Method to add a task to the list and database
    private void addTask(String taskName) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskDatabaseHelper.COLUMN_TASK, taskName);
        values.put(TaskDatabaseHelper.COLUMN_COMPLETED, 0);
        long id = db.insert(TaskDatabaseHelper.TABLE_TASKS, null, values);
        db.close();

        if (id != -1) {
            Task task = new Task();
            task.setId((int) id);
            task.setTask(taskName);
            task.setCompleted(false);
            taskList.add(0, task);
            taskListAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to load tasks from the database
    private void loadTasks() {
        taskList.clear();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskDatabaseHelper.TABLE_TASKS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_ID)));
                task.setTask(cursor.getString(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_TASK)));
                task.setCompleted(cursor.getInt(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_COMPLETED)) == 1);
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    // Method to delete the checked tasks from the list and database
    private void deleteCheckedTasks() {
        List<Task> selectedTasks = new ArrayList<>();

        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            if (task.isCompleted()) {
                selectedTasks.add(task);
            }
        }

        if (!selectedTasks.isEmpty()) {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            for (Task task : selectedTasks) {
                db.delete(TaskDatabaseHelper.TABLE_TASKS, TaskDatabaseHelper.COLUMN_ID + " = ?",
                        new String[]{String.valueOf(task.getId())});
                taskList.remove(task);
            }
            db.close();
            taskListAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No tasks selected for deletion", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to generate a unique ID for each task
    private int generateTaskId() {
        int maxId = 0;
        for (Task task : taskList) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        return maxId + 1;
    }
}
