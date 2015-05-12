package tv.organicinterac.FitTrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * Created by Paul on 4/7/2015.
 */
public class NewExerciseActivity extends ActionBarActivity {
    EditText mExerciseName, mExerciseSets, mExerciseReps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);

        mExerciseName = (EditText) findViewById(R.id.exercise_name_edittext);
        mExerciseSets = (EditText) findViewById(R.id.sets_edittext);
        mExerciseReps = (EditText) findViewById(R.id.reps_edittext);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_new_workout, menu);
        return true;
    }

    private void sendResults(String name, int sets, int reps) {
        Intent data = new Intent();

        data.putExtra("name", name);
        data.putExtra("sets", sets);
        data.putExtra("reps", reps);
        setResult(Activity.RESULT_OK, data);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save_workout) {
            int sets, reps;
            try {
                sets = Integer.valueOf(mExerciseSets.getText().toString());
                reps = Integer.valueOf(mExerciseReps.getText().toString());
                mExerciseName.getText().toString().charAt(0);
                sendResults(mExerciseName.getText().toString(), sets, reps);
            } catch (Exception e) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
