package tv.organicinterac.FitTrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Paul on 5/15/2015.
 */
public class StartWorkoutActivity extends ActionBarActivity {
    private ListView mWorkoutsListView;
    StartWorkoutWorkoutsItemAdapter mAdapter;
    private List<Long> mWorkoutIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);
        mWorkoutsListView = (ListView) findViewById(R.id.workouts_listview);
        mAdapter = new StartWorkoutWorkoutsItemAdapter(this, R.layout.start_workout_list_item, getWorkoutTitlesAndSetIDs());
        mWorkoutsListView.setAdapter(mAdapter);
        mWorkoutsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView selectedText = (TextView) mWorkoutsListView.getChildAt(position).findViewById(R.id.workout_name_textview);
//                String selected = selectedText.getText().toString();
//        //TODO: make launch the running activity
//
//                toast(selected);
                startRunningWorkout(mWorkoutIds.get(position));
            }
        });
    }

    private void startRunningWorkout(long workoutId) {
        Intent intent = new Intent(this, RunningWorkoutActivity.class);
        intent.putExtra("workout_id", workoutId);
        startActivity(intent);
    }

    public List<String[]> getWorkoutTitlesAndSetIDs() {
        DatabaseInteraction di = new DatabaseInteraction(this);
        List<String[]> rawWorkouts = di.getWorkouts();
        ArrayList<String[]> workoutTitles = new ArrayList<String[]>();
        mWorkoutIds = new ArrayList<Long>();
        for(String[] rawWorkout: rawWorkouts) {
            workoutTitles.add(new String[]{rawWorkout[1]});
            mWorkoutIds.add(Long.parseLong(rawWorkout[0]));
//            workoutTitles.add(new String[]{Arrays.toString(rawWorkout)});
        }
        return workoutTitles;
    }
    private void toast(String out) {
        //cuz I'm real lazy like that
        Toast.makeText(this, out, Toast.LENGTH_SHORT).show();
    }
}
