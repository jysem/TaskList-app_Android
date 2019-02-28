package app.taskList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseAdapter {

    private static final String DATABASE_NAME = "TaskListDB.db";
    private static final String TABLE_NAME = "TaskList";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_INFO = "info";

    private SQLiteDatabase db;
    private DatabaseOpenHelper dbHelper;
    private Cursor cursor;
    private long dbStatus;

    public DatabaseAdapter(Context context) {
        dbHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null)
            db.close();
    }

    public Boolean insertNewTask(String date, String title, String info) {

        ContentValues newTask = new ContentValues();

        date = convertDateStamp(date);

        newTask.put(COLUMN_DATE, date);
        newTask.put(COLUMN_TITLE, title);
        newTask.put(COLUMN_INFO, info);

        open();
        dbStatus = db.insert(TABLE_NAME, null, newTask);
        close();
        if (dbStatus == -1)
            return false;
        else
            return true;
    }

    public Boolean updateTask(Task task) {

        ContentValues editTask = new ContentValues();
        editTask.put(COLUMN_DATE, convertDateStamp(task.getDate()));
        editTask.put(COLUMN_TITLE, task.getTitle());
        editTask.put(COLUMN_INFO, task.getBrief());

        int id = task.getId();

        open();
        dbStatus = db.update(TABLE_NAME, editTask, COLUMN_ID+"="+id, null);
        close();

        if (dbStatus == -1)
            return false;
        else
            return true;
    }

    public Boolean deleteTask(int id) {
        open();
        dbStatus = db.delete(TABLE_NAME,COLUMN_ID+"="+id, null);
        close();

        if (dbStatus == -1)
            return false;
        else
            return true;
    }


    public Task getTask(long id) {

        open();
        cursor = db.query(TABLE_NAME, null, COLUMN_ID+"="+id,null,null,null,null);
        close();

        Task task = new Task();
        task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        task.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
        task.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        task.setBrief(cursor.getString(cursor.getColumnIndex(COLUMN_INFO)));

        return task;
    }

    public ArrayList<Task> getAllTasks(String sortBy) {
        ArrayList<Task> taskList = new ArrayList<>();
        if (sortBy == "")
            sortBy = "date";

        open();
        cursor = db.query(TABLE_NAME, null, null, null,null, null, sortBy);

        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
                task.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                task.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                task.setBrief(cursor.getString(cursor.getColumnIndex(COLUMN_INFO)));

                task.setDate(convertDate(task.getDate()));

                taskList.add(task);
                cursor.moveToNext();
            } while (cursor.moveToNext());
        }
        close();
        return taskList;
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper {

        public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CreateQuery =
                    "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + COLUMN_DATE + " DATE, "
                    + COLUMN_TITLE + " TEXT, "
                    + COLUMN_INFO + " TEXT)";

            db.execSQL(CreateQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        }
    }

    public String convertDateStamp(String date) {
        String[] tempDate = new String[3];
        String tempString = new String();
        int i, j = 0;

        for (i = 0; i < date.length(); i++) {
            if (date.charAt(i) != '.' && date.charAt(i) != '-' && date.charAt(i) != '/' && date.charAt(i) != ':') {
                tempString += date.charAt(i);
            }
            else if (date.charAt(i) == '.' || date.charAt(i) == '-' || date.charAt(i) == '/' || date.charAt(i) == ':') {
                tempDate[j] = tempString;
                tempString = "";
                j++;
            }
        }

        tempDate[j] = tempString;   // tempYear
        // CONVERT DD TO 01 & MM TO 01 IF UNDER 10
        if (tempDate[0].length() < 2) {
            tempDate[0] = "0" + tempDate[0];
        }
        if (tempDate[1].length() < 2) {
            tempDate[1] = "0" + tempDate[1];
        }

        // COVERT DATE TO TIMESTAMP FORMAT YYYY-MM-YY
        date = tempDate[2] + "-" + tempDate[1] + "-" + tempDate[0];
        return date;
    }

    public String convertDate(String timeStamp) {
        int i, j = 2;
        String[] tempDate = new String[3];
        String tempString = new String();

        for (i = 0; i < timeStamp.length(); i++) {
            if (timeStamp.charAt(i) != '-')
                tempString += timeStamp.charAt(i);
            else {
                tempDate[j] = tempString;
                tempString = "";
                j--;
            }
        }
        tempDate[j] = tempString;

        tempString = tempDate[0] + "." + tempDate[1] + "." + tempDate[2];
        return tempString;
    }
}



