package app.taskList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SortMenuAdapter extends ArrayAdapter<SortMenuItem> {

    public SortMenuAdapter(Context context, ArrayList<SortMenuItem> sortList) {
        super(context, 0, sortList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View covertView, @NonNull ViewGroup parent) {
        return initView(position, covertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sort_menu_spinner, parent, false);
        }
        ImageView img_SortMenu = convertView.findViewById(R.id.image_sort_menu);
        TextView txt_SortMenu = convertView.findViewById(R.id.text_sort_menu);

        SortMenuItem currentItem = getItem(position);

        if (currentItem != null) {
            img_SortMenu.setImageResource(currentItem.getSortByImg());
            txt_SortMenu.setText(currentItem.getSortBy());
        }
        return convertView;
    }
}
