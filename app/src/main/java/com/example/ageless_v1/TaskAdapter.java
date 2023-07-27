package com.example.ageless_v1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends BaseAdapter {

    private Context context;
    private List<Task> taskList;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.checkbox = convertView.findViewById(R.id.taskCheckBox);
            viewHolder.taskName = convertView.findViewById(R.id.taskDescription);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Task task = taskList.get(position);
        viewHolder.taskName.setText(task.getTask());
        viewHolder.checkbox.setChecked(task.isCompleted());

        viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkbox = (CheckBox) v;
                task.setCompleted(checkbox.isChecked());
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        CheckBox checkbox;
        TextView taskName;
    }
}

