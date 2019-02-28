package app.taskList;

public class Task {

    public static final String TABLE_NAME = "task_table";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_BRIEF = "brief";

    private int id;
    private String date;
    private String title;
    private String brief;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DATE + " TASK DATE, "
            + COLUMN_TITLE + "TASK TITLE, "
            + COLUMN_BRIEF + "TASK INFO"
            + ")";

    public Task() {
    }

    public Task(int id, String date, String title, String brief) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.brief = brief;
    }

    public int getId() {
        return id;
    }
    public String getDate() {
        return date;
    }
    public String getTitle() {
        return title;
    }
    public String getBrief() {
        return brief;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setBrief(String brief) {
        this.brief = brief;
    }




}
