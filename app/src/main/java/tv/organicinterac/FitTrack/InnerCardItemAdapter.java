package tv.organicinterac.FitTrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Paul on 3/24/2015.
 */
public class InnerCardItemAdapter extends ArrayAdapter<String[]> {
    TextView tvExerciseName;

    public InnerCardItemAdapter(Context context, int resource, List<String[]> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.mini_past_workout_exercise_list_item, null);
        }

        String[] p = getItem(position);
        if (p.length > 0) {
            tvExerciseName = (TextView) v.findViewById(R.id.exercise_name_textview);
            tvExerciseName.setText(p[0]);
        }
        return v;
    }
}
