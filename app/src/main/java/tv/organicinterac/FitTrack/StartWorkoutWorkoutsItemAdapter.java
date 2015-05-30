package tv.organicinterac.FitTrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Paul on 5/29/2015.
 */
public class StartWorkoutWorkoutsItemAdapter extends ArrayAdapter<String[]> {
    public StartWorkoutWorkoutsItemAdapter(Context context, int resource, List<String[]> items) {
        super(context, resource, items);
    }
    TextView mWorkoutNameTextView;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.start_workout_list_item, null);
        }
        String[] p = getItem(position);

        if (p.length == 1) {
            mWorkoutNameTextView = (TextView) v.findViewById(R.id.workout_name_textview);
            mWorkoutNameTextView.setText(p[0]);
        }
        return v;
    }
}
