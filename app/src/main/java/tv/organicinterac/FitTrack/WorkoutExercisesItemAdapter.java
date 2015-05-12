package tv.organicinterac.FitTrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Paul on 4/17/2015.
 */
public class WorkoutExercisesItemAdapter extends ArrayAdapter<String[]> {
    TextView mExerciseName, mSetsReps;

    public WorkoutExercisesItemAdapter(Context context, int resource, List<String[]> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.new_exercise_list_item, null);


        }
        String[] p = getItem(position);
        if (p.length == 3) {
            mExerciseName = (TextView) v.findViewById(R.id.exercise_name_textview);
            mSetsReps = (TextView) v.findViewById(R.id.exercise_sets_reps_textview);
            mExerciseName.setText(p[0]);
            mSetsReps.setText(formatSetsReps(p[1], p[2]));
        }

        return v;
    }

    private String formatSetsReps(String sets, String reps) {
        return sets + " x " + reps;
    }

    @Override
    public void add(String[] object) {
        super.add(object);
    }
}
