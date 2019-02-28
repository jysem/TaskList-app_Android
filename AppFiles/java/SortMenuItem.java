package app.taskList;

public class SortMenuItem {
    private String mSortBy;
    private int mSortByImg;

    public SortMenuItem(String SortBy, int SortByImg) {
        mSortBy = SortBy;
        mSortByImg = SortByImg;
    }

    public String getSortBy() {
        return mSortBy;
    }

    public int getSortByImg() {
        return mSortByImg;
    }
}
