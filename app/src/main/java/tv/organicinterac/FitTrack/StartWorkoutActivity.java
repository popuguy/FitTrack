package tv.organicinterac.FitTrack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

/**
 * Created by Paul on 5/15/2015.
 */
public class StartWorkoutActivity extends ActionBarActivity {
    private ListView mWorkoutsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);
        mWorkoutsListView = (ListView) findViewById(R.id.workouts_listview);
        mWorkoutsListView.setAdapter();
    }
}
