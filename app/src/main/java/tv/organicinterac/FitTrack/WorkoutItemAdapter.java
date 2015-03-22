package tv.organicinterac.FitTrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Paul on 3/7/2015.
 */
public class WorkoutItemAdapter extends ArrayAdapter<String[]> {
    public WorkoutItemAdapter(Context context, int resource, List<String[]> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.list_item, null);
        }
        String[] p = getItem(position);

        if (p.length == 2) {
            CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox);
            TextView tv = (TextView) v.findViewById(R.id.textView);
            if (cb != null) {
                cb.setText(p[0]);
            }
            if (tv != null) {
                tv.setText(p[1]);
            }
        }
        return v;
    }
}
