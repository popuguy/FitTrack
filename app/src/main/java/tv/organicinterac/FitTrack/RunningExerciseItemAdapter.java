package tv.organicinterac.FitTrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Paul on 5/30/2015.
 */
public class RunningExerciseItemAdapter extends ArrayAdapter<String[]> {
    private Context mContext;
    public RunningExerciseItemAdapter(Context context, int resource, List<String[]> items) {
        super(context, resource, items);
        mContext = context;
    }
    TextView mExerciseNameTextView, mExerciseSetsRepsTextView;
    CheckBox mCB1, mCB2, mCB3, mCB4, mCB5;
    int mSets;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null)  {
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.running_exercise_list_item, null);

        }
        String[] p = getItem(position);

        if (p.length == 3) {
            mExerciseNameTextView = (TextView) v.findViewById(R.id.exercise_name_textview);
            mExerciseSetsRepsTextView = (TextView) v.findViewById(R.id.exercise_sets_reps_textview);
            mCB1 = (CheckBox) v.findViewById(R.id.set1_checkbox);
            mCB2 = (CheckBox) v.findViewById(R.id.set2_checkbox);
            mCB3 = (CheckBox) v.findViewById(R.id.set3_checkbox);
            mCB4 = (CheckBox) v.findViewById(R.id.set4_checkbox);
            mCB5 = (CheckBox) v.findViewById(R.id.set5_checkbox);
            CheckBox[] cbs = new CheckBox[]{mCB5, mCB4, mCB3, mCB2, mCB1};
            String exerciseName = p[0];
            String setsReps = p[1] + "x" + p[2];
            int sets = Integer.valueOf(p[1]);
            mExerciseNameTextView.setText(exerciseName);
            mExerciseSetsRepsTextView.setText(setsReps);
            if (sets > 5 || sets < 1)
                sets = 1;
            mSets = sets;
            for (CheckBox cb: cbs) {
                if(sets > 4)
                    break;
                sets += 1;
                cb.setVisibility(View.GONE);
            }
            for (CheckBox cb: cbs) {
                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mContext instanceof RunningWorkoutActivity &&
                                ((CheckBox)v).isChecked()) {

                            ((RunningWorkoutActivity)mContext).startNewBreakCountDown();
                        }
                    }
                });
            }
        }
        return v;
    }
}
