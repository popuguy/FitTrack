package tv.organicinterac.FitTrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 4/6/2015.
 */
public class NewWorkoutActivity extends ActionBarActivity {
    private static final int NEW_EXERCISE_REQUEST = 1;
    private ListView mExercisesListView;
    private Button mAddExercise;
    private EditText mWorkoutTitle;
    private WorkoutExercisesItemAdapter mAdapter;
    private ArrayList<Exercise> mReturnExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        mReturnExercises = new ArrayList<Exercise>();

        mWorkoutTitle = (EditText) findViewById(R.id.workout_title_edittext);
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
                Exercise newExercise = new Exercise();
                newExercise.setName(name);
                newExercise.setSets(sets);
                newExercise.setReps(reps);
                mReturnExercises.add(newExercise);
                mAdapter.add(new String[]{name, Integer.toString(sets), Integer.toString(reps)});
            } else if (resultCode == RESULT_CANCELED) {
                toast("Invalid values submitted; exercise not saved");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save_workout) {
            sendResults();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendResults() {
        Intent data = new Intent();

        if (mReturnExercises.size() > 0) {
            data.putExtra("title", mWorkoutTitle.getText().toString());
//            data.putExtra("exercises", mReturnExercises);
            data.putParcelableArrayListExtra("exercises", mReturnExercises);
//            data.putParcelableArrayListExtra()
            setResult(Activity.RESULT_OK, data);
        } else {
            setResult(Activity.RESULT_CANCELED, data);
        }

        finish();
    }

    private void toast(String out) {
        //cuz I'm real lazy like that
        Toast.makeText(this.getApplicationContext(), out, Toast.LENGTH_SHORT).show();
    }

    public class Exercise implements Parcelable {
        private int sets, reps;
        private String name;

        public int getSets() {
            return sets;
        }
        public int getReps() {
            return reps;
        }
        public void setSets(int sets) {
            this.sets = sets;
        }
        public void setReps(int reps) {
            this.reps = reps;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int describeContents() {
            return this.hashCode();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(sets);
            dest.writeInt(reps);
            dest.writeString(name);
        }
    }

//    class Exercise implements Serializable {
//        int sets, reps;
//        String name;
//        Exercise(String n, int s, int r) {
//            name = n;
//            sets = s;
//            reps = r;
//        }
//    }

}
