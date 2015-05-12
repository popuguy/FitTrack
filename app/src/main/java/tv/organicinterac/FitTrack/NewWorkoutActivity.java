package tv.organicinterac.FitTrack;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 4/6/2015.
 */
public class NewWorkoutActivity extends ActionBarActivity {
    private static final int NEW_EXERCISE_REQUEST = 1;
    private ListView mExercisesListView;
    private Button mAddExercise;
    private WorkoutExercisesItemAdapter mAdapter;
    private List<String[]> mExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        mExercises = new ArrayList<String[]>();


        mExercisesListView = (ListView) findViewById(R.id.exercises_add_listview);
        mAddExercise = (Button) findViewById(R.id.add_exercise_button);
        mAdapter = new WorkoutExercisesItemAdapter(this, R.layout.new_exercise_list_item, new ArrayList<String[]>());
        mExercisesListView.setAdapter(mAdapter);

        mAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(NewWorkoutActivity.this, NewExerciseActivity.class), NEW_EXERCISE_REQUEST);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_new_workout, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_EXERCISE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle results = data.getExtras();
                String name = results.getString("name");
                int sets = results.getInt("sets");
                int reps = results.getInt("reps");
                mExercises.add(new String[]{name, Integer.toString(sets), Integer.toString(reps)});
                mAdapter.add(new String[]{name, Integer.toString(sets), Integer.toString(reps)});
            } else if (resultCode == RESULT_CANCELED) {
                toast("Invalid values submitted; exercise not saved");
            }
        }
    }
    private void toast(String out) {
        //cuz I'm real lazy like that
        Toast.makeText(this.getApplicationContext(), out, Toast.LENGTH_SHORT).show();
    }


}
