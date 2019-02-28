package app.taskList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<Task> {

    private final Context context;
    private ArrayList<Task> taskList = new ArrayList<>();

    public MyListAdapter(Context _context, ArrayList<Task> _taskList) {
        super(_context, R.layout.list_item, _taskList);
        this.context = _context;
        this.taskList = _taskList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, parent, false);
        }
        TextView dateText = (TextView) view.findViewById(R.id.taskDate);
        TextView titleText = (TextView) view.findViewById(R.id.taskTitle);

        dateText.setText(taskList.get(position).getDate());
        titleText.setText(taskList.get(position).getTitle());

        return view;
    }
}
