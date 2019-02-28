package app.taskList;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.taskList.DatabaseAdapter;
import app.taskList.MyListAdapter;
import app.taskList.SortMenuAdapter;
import app.taskList.SortMenuItem;
import app.taskList.Task;

public class MainActivity extends AppCompatActivity {

    private ArrayList<SortMenuItem> mSortMenuList;
    private SortMenuAdapter mSortMenuAdapter;

    private ListView listView;
    private DatabaseAdapter db;
    private ArrayList<Task> taskList = new ArrayList<Task>();

    private MyListAdapter adapter;
    private Dialog CurrentDialog;
    private String sortBy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseAdapter(this);
        taskList = db.getAllTasks(sortBy);
        adapter = new MyListAdapter(MainActivity.this, taskList);

        populateData();

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewTaskDialog(taskList.get(position));
            }
        });

        // ----------------------------------------------------
        // NEW TASK BUTTON - DIALOG
        // ----------------------------------------------------
        final Button newTask = (Button)findViewById(R.id.btn_NewTask);
        newTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTaskDialog();
            }
        });


        // ----------------------------------------------------
        // SPINNER FUNCTIONS
        // ----------------------------------------------------
        initList();

        final Spinner spinner_SortMenu = findViewById(R.id.spn_Sort);

        mSortMenuAdapter = new SortMenuAdapter(this, mSortMenuList);
        spinner_SortMenu.setAdapter(mSortMenuAdapter);

        spinner_SortMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SortMenuItem clickedSortMenuItem = (SortMenuItem) parent.getItemAtPosition(position);
                if (clickedSortMenuItem.getSortBy() == "By Date") {
                    Toast.makeText(MainActivity.this, "Tasks sorted by Date", Toast.LENGTH_SHORT).show();
                    sortBy = "date";
                    populateData();
                }
                else if (clickedSortMenuItem.getSortBy() == "By Alphabet") {
                    Toast.makeText(MainActivity.this, "Tasks sorted by Alphabet", Toast.LENGTH_SHORT).show();
                    sortBy = "title";
                    populateData();
                }
                spinner_SortMenu.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initList() {
        mSortMenuList = new ArrayList<>();
        mSortMenuList.add(new SortMenuItem("Sort", R.drawable.sort));
        mSortMenuList.add(new SortMenuItem("By Date", R.drawable.calendar));
        mSortMenuList.add(new SortMenuItem("By Alphabet", R.drawable.alphabet_sort));
    }

    private void ViewTaskDialog(final Task task) {

        CurrentDialog = new Dialog(this);
        CurrentDialog.setContentView(R.layout.view_task_dialog);

        TextView viewDate = (TextView) CurrentDialog.findViewById(R.id.viewTaskDate);
        TextView viewTitle = (TextView) CurrentDialog.findViewById(R.id.viewTaskTitle);
        TextView viewInfo = (TextView) CurrentDialog.findViewById(R.id.viewTaskInfo);

        viewDate.setText(task.getDate());
        viewTitle.setText(task.getTitle());
        viewInfo.setText(task.getBrief());

        Button edit = (Button) CurrentDialog.findViewById(R.id.btn_Edit);
        Button delete = (Button) CurrentDialog.findViewById(R.id.btn_Delete);
        Button cancel = (Button) CurrentDialog.findViewById(R.id.btn_Cancel);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentDialog.dismiss();
                EditTaskDialog(task);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteTask(task.getId());
                Toast.makeText(MainActivity.this, "Task deleted: " + task.getDate() + " - " + task.getDate(), Toast.LENGTH_SHORT).show();
                CurrentDialog.dismiss();
                populateData();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentDialog.dismiss();
            }
        });

        CurrentDialog.show();
    }

    private void EditTaskDialog(final Task task) {

        CurrentDialog = new Dialog(this);
        CurrentDialog.setContentView(R.layout.edit_task_dialog);

        final EditText editDate = (EditText) CurrentDialog.findViewById(R.id.editText_edit_date);
        final EditText editTitle = (EditText) CurrentDialog.findViewById(R.id.editText_edit_title);
        final EditText editInfo = (EditText) CurrentDialog.findViewById(R.id.editText_edit_brief);

        editDate.setText(task.getDate());
        editTitle.setText(task.getTitle());
        editInfo.setText(task.getBrief());

        Button update = (Button) CurrentDialog.findViewById(R.id.btn_Update);
        Button cancel = (Button) CurrentDialog.findViewById(R.id.btn_Cancel);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDate = editDate.getText().toString();
                String newTitle = editTitle.getText().toString();
                String newInfo = editInfo.getText().toString();

                Boolean dateCheck = checkDate(newDate);

                if (newTitle.isEmpty() || newDate.isEmpty() || newInfo.isEmpty() || dateCheck == false) {
                    if (newTitle.isEmpty())
                        Toast.makeText(MainActivity.this,"Title missing", Toast.LENGTH_SHORT).show();
                    else if (newDate.isEmpty())
                        Toast.makeText(MainActivity.this,"Date missing", Toast.LENGTH_SHORT).show();
                    else if (dateCheck == false)
                        Toast.makeText(MainActivity.this,"Invalid date format", Toast.LENGTH_SHORT).show();
                    else if (newInfo.isEmpty())
                        Toast.makeText(MainActivity.this,"Info missing", Toast.LENGTH_SHORT).show();
                }

                else {
                    task.setDate(newDate);
                    task.setTitle(newTitle);
                    task.setBrief(newInfo);
                    db.updateTask(task);
                    Toast.makeText(MainActivity.this, "Task updated successfully!", Toast.LENGTH_SHORT).show();
                    CurrentDialog.dismiss();
                    populateData();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentDialog.dismiss();
            }
        });

        CurrentDialog.show();
    }

    private void NewTaskDialog() {

        CurrentDialog = new Dialog(this);
        CurrentDialog.setContentView(R.layout.new_task_dialog);

        final EditText insertDate = (EditText) CurrentDialog.findViewById(R.id.editText_new_date);
        final EditText insertTitle = (EditText) CurrentDialog.findViewById(R.id.editText_new_title);
        final EditText insertInfo = (EditText) CurrentDialog.findViewById(R.id.editText_new_brief);

        Button submit = (Button) CurrentDialog.findViewById(R.id.btn_Submit);
        Button cancel = (Button) CurrentDialog.findViewById(R.id.btn_Cancel);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDate = insertDate.getText().toString();
                String newTitle = insertTitle.getText().toString();
                String newInfo = insertInfo.getText().toString();

                Boolean dateCheck = checkDate(newDate);

                if (newTitle.isEmpty() || newDate.isEmpty() || newInfo.isEmpty() || dateCheck == false) {
                    if (newTitle.isEmpty())
                        Toast.makeText(MainActivity.this,"Title missing", Toast.LENGTH_SHORT).show();
                    else if (newDate.isEmpty())
                        Toast.makeText(MainActivity.this,"Date missing", Toast.LENGTH_SHORT).show();
                    else if (dateCheck == false)
                        Toast.makeText(MainActivity.this,"Invalid date format", Toast.LENGTH_SHORT).show();
                    else if (newInfo.isEmpty())
                        Toast.makeText(MainActivity.this,"Info missing", Toast.LENGTH_SHORT).show();
                }

                else {
                    if (db.insertNewTask(newDate, newTitle, newInfo)) {
                        Toast.makeText(MainActivity.this,
                                "New Task Added: " + newDate + " - " + newTitle + "\n" + newInfo,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Database insert error", Toast.LENGTH_SHORT).show();
                    }
                    CurrentDialog.dismiss();
                    populateData();

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentDialog.dismiss();
            }
        });

        CurrentDialog.show();

    }

    public Boolean checkDate(String date) {
        String tempDate = "";

        // CHECK INVALID CHARS
        for (int i = 0; i < date.length(); i++) {
            if(date.charAt(i) != '.' || date.charAt(i) != ':' || date.charAt(i) != '/' || date.charAt(i) != '-') {
                if (date.charAt(i) < '0' && date.charAt(i) > '9') {
                    return false;
                }
            }

        }

        // CHECK YEAR LENGTH
        for (int i = 0; i < date.length(); i++) {
            if (date.charAt(i) == '.' || date.charAt(i) == '-' || date.charAt(i) == '/' || date.charAt(i) == ':') {
                tempDate = "";
            }
            else {
                tempDate += date.charAt(i);
            }
        }

        // YEAR LENGTH < 4 digit
        if (tempDate.length() < 4) {
            return false;
        }

        return true;
    }

    public void populateData() {
        taskList = db.getAllTasks(sortBy);

        if (adapter != null)
            adapter.clear();

        adapter.addAll(taskList);
        adapter.notifyDataSetChanged();
    }

}
