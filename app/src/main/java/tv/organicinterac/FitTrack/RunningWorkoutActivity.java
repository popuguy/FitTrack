package tv.organicinterac.FitTrack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 5/30/2015.
 */
public class RunningWorkoutActivity extends ActionBarActivity {
    private Button mFinishButton;
    private ListView mExercisesListView;
    private TextView mRunTimeTextView, mBreakTimeTextView;
    private static final long REST_BETWEEN_SETS = 91000; //milliseconds
    private List<String[]> mRawExercises;

    long startBreakTime = 0;
    long startTime = 0;
    long timeBreakEnds;
    List<Integer> setsPerExercise;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_workout);

        //makes custom actionbar with timers, may break UI uniformity in later android updates

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_HOME_AS_UP
                | ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(R.layout.actionbar_running_workout);

        mFinishButton = (Button)findViewById(R.id.finish_button);
        mRunTimeTextView = (TextView)actionBar.getCustomView().findViewById(R.id.run_time_textview);
        mBreakTimeTextView = (TextView)actionBar.getCustomView().findViewById(
                R.id.break_time_textview);
        mBreakTimeTextView.setText("BREAK 0:00");
        mExercisesListView = (ListView)findViewById(R.id.exercises_listview);
        List<String[]> rawExercises = getExercises();
        mRawExercises = rawExercises;

        setsPerExercise = new ArrayList<Integer>();
        ArrayList<String[]> items = new ArrayList<String[]>();
        for (String[] rawExercise: rawExercises) {
            items.add(new String[]{rawExercise[0], rawExercise[1], rawExercise[2]});
            setsPerExercise.add(Integer.valueOf(rawExercise[1]));
        }
        final RunningExerciseItemAdapter adapter = new RunningExerciseItemAdapter(this,
                R.layout.running_exercise_list_item, items);
        mExercisesListView.setAdapter(adapter);

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkout();
                Intent data = new Intent();

                setResult(Activity.RESULT_OK, data);
                finish();


                //perform database interaction
            }
        });

        startTime = System.currentTimeMillis();
//        run.run();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                updateTimer();
                handler.postDelayed(this, 20);
            }
        }, 20);
    }
    private int getNumBoxesChecked(int index) {
        View view = mExercisesListView.getChildAt(index);
        int count = 0;
        if (((CheckBox)view.findViewById(R.id.set1_checkbox)).isChecked())
            count += 1;
        if (((CheckBox)view.findViewById(R.id.set2_checkbox)).isChecked())
            count += 1;
        if (((CheckBox)view.findViewById(R.id.set3_checkbox)).isChecked())
            count += 1;
        if (((CheckBox)view.findViewById(R.id.set4_checkbox)).isChecked())
            count += 1;
        if (((CheckBox)view.findViewById(R.id.set5_checkbox)).isChecked())
            count += 1;
        return count;
    }
    private List<String[]> getExercises() {
        DatabaseInteraction di = new DatabaseInteraction(this);
        long workoutId = getIntent().getExtras().getLong("workout_id");
        List<String[]> exercises = di.getExercisesByWorkout(workoutId);
        di.completeInteraction();
        return exercises;
    }
    public void startNewBreakCountDown() {
        timeBreakEnds = System.currentTimeMillis() + REST_BETWEEN_SETS;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateRestTimer();
                if (!(timeBreakEnds <= System.currentTimeMillis()))
                    handler.postDelayed(this, 20);
            }
        }, 20);
    }
    public void test() {
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT);
        System.out.println("test() method called");
        mRunTimeTextView.setText("changed");
    }
    private void updateTimer() {
        long millis = System.currentTimeMillis() - startTime;
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds     = seconds % 60;
        mRunTimeTextView.setText(
                String.format("%d:%02d.%02d",
                        minutes,
                        seconds,
                        (int)millis % 100));
    }
    private void updateRestTimer() {
        long millis = timeBreakEnds - System.currentTimeMillis();
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds     = seconds % 60;
        mBreakTimeTextView.setText(String.format("BREAK %d:%02d", minutes, seconds));
    }

    private void saveWorkout() {
        DatabaseInteraction di = new DatabaseInteraction(this);
        long workoutId = getIntent().getExtras().getLong("workout_id");
        long timeElapsed = System.currentTimeMillis() - startTime;
        long completeWorkoutId = di.addCompleteWorkout(workoutId, timeElapsed,
                Long.toString(System.currentTimeMillis()));
        for (int i = 0; i < mRawExercises.size(); i += 1) {
            if (getNumBoxesChecked(i) == Integer.getInteger(mRawExercises.get(i)[1])
                    || (getNumBoxesChecked(i) == 1
                    && Integer.getInteger(mRawExercises.get(i)[1]) == 1)) {
                di.addCompleteExercise(completeWorkoutId, Long.parseLong(mRawExercises.get(i)[4]));
            }

        }

        di.completeInteraction();
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT);
    }
    private void completeWorkout() {
        long workoutTime = System.currentTimeMillis() - startTime;
        long workoutId = getIntent().getExtras().getLong("workout_id");
    }
}
