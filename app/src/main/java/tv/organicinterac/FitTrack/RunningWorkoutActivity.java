package tv.organicinterac.FitTrack;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
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
    ListView mExercisesListView;
    TextView mRunTimeTextView, mBreakTimeTextView;
    private static final long REST_BETWEEN_SETS = 91000; //milliseconds
    long startBreakTime = 0;
    long startTime = 0;
    long timeBreakEnds;

    final Handler h = new Handler(new Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds     = seconds % 60;

            mRunTimeTextView.setText(String.format("%d:%02d.%02d", minutes, seconds, (int)millis));
            return false;
        }
    });
    Runnable run = new Runnable() {

        @Override
        public void run() {
            System.out.println("runnable runnabling");

            h.postDelayed(this, 10);
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_workout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_HOME_AS_UP
                | ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(R.layout.actionbar_running_workout);
        mRunTimeTextView = (TextView)actionBar.getCustomView().findViewById(R.id.run_time_textview);
        mBreakTimeTextView = (TextView)actionBar.getCustomView().findViewById(
                R.id.break_time_textview);
        mBreakTimeTextView.setText("BREAK 0:00");
        mExercisesListView = (ListView)findViewById(R.id.exercises_listview);
        List<String[]> rawExercises = getExercises();

        ArrayList<String[]> items = new ArrayList<String[]>();
//        items.add(new String[]{"Name", "3", "3"});
        for (String[] rawExercise: rawExercises) {
            items.add(new String[]{rawExercise[0], rawExercise[1], rawExercise[2]});
        }
        RunningExerciseItemAdapter adapter = new RunningExerciseItemAdapter(this,
                R.layout.running_exercise_list_item, items);
        mExercisesListView.setAdapter(adapter);

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
    private List<String[]> getExercises() {
        DatabaseInteraction di = new DatabaseInteraction(this);
        long workoutId = getIntent().getExtras().getLong("workout_id");
        return di.getExercisesByWorkout(workoutId);
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
}
