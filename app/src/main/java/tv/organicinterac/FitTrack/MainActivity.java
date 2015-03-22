package tv.organicinterac.FitTrack;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity {

    ImageButton bStart;
    Button bAdd;
    TextView tvTimer;
    ListView lvWorkoutItems;
    RelativeLayout rlNewEvent;
    EditText etNewExerciseName, etNewExerciseSetsName, etNewExerciseRepsName;

    WorkoutAdapter adapter;
    WorkoutItemAdapter wia;
    Thread thread;
    Date startRun;
    String origText;
    boolean running = false;

    List<String[]> rawItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getActionBar().hide();

//        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_main);
        bStart = (ImageButton) findViewById(R.id.start_button);
        bAdd = (Button) findViewById(R.id.button);
        tvTimer = (TextView) findViewById(R.id.timer_textview);
        origText = tvTimer.getText().toString();
        lvWorkoutItems = (ListView) findViewById(R.id.workout_items_listview);
        rlNewEvent = (RelativeLayout) findViewById(R.id.new_event_relativelayout);
        etNewExerciseName = (EditText) findViewById(R.id.new_exercise_name_edittext);
        etNewExerciseSetsName = (EditText) findViewById(R.id.new_exercise_sets_edittext);
        etNewExerciseRepsName = (EditText) findViewById(R.id.new_exercise_reps_edittext);
        ArrayList<String[]> items = new ArrayList<>();
        adapter = new WorkoutAdapter(getApplicationContext());
        rawItems = adapter.getExercises();
        for (String[] rawItem: rawItems) {
            items.add(new String[]{rawItem[1], rawItem[2]});
        }
        wia = new WorkoutItemAdapter(this, R.layout.list_item, items);

        lvWorkoutItems.setAdapter(wia);
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rlNewEvent.getVisibility() != View.VISIBLE) {
                    rlNewEvent.setVisibility(View.VISIBLE);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(rlNewEvent, "alpha", 0f, 1f);
                    animator.setDuration(550);
                    animator.start();
                } else {
                    String[] newExercise = getNewExercise();
                    if (newExercise[0].length() > 0 && newExercise[1].length() > 4){
                        InputMethodManager imm = (InputMethodManager)getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etNewExerciseRepsName.getWindowToken(), 0);
                        wia.add(newExercise);
                        adapter.addExercise(newExercise[0], newExercise[1]);
                        clearExerciseEntry();
                        ObjectAnimator animator = ObjectAnimator.ofFloat(rlNewEvent, "alpha", 1f, 0f);
                        animator.setDuration(550);
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                rlNewEvent.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        animator.setStartDelay(200);
                        animator.start();
                    }
                }
            }
        });
        Timer textUpdateTimer = new Timer();
        textUpdateTimer.scheduleAtFixedRate(new TimerTask(){

            @Override
            public void run() {
                if (running) {
                    final int diffHour = (int)TimeUnit.MILLISECONDS.toHours(
                            (new Date()).getTime() - startRun.getTime());
                    final int diffMin = (int)TimeUnit.MILLISECONDS.toMinutes(
                            (new Date()).getTime() - startRun.getTime()) -
                            (diffHour * 60);
                    final int diffSec = (int)TimeUnit.MILLISECONDS.toSeconds(
                            (new Date()).getTime() - startRun.getTime()) -
                            (diffMin * 60) - (diffHour * 60 * 60);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvTimer.setText(String.format("Current time: %02d:%02d:%02d",
                                    diffHour, diffMin, diffSec));
                        }
                    });
                }
            }
        }, 1000, 1000);
    }


    public void startTimer(View v) {
        if (running) {
            running = false;
        } else {
            startRun = new Date();
            running = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private String[] getNewExercise() {
        return new String[]{etNewExerciseName.getText().toString(),
                etNewExerciseSetsName.getText().toString() + " x " +
                        etNewExerciseRepsName.getText().toString()};
    }

    private void clearExerciseEntry() {
        etNewExerciseName.setText("");
        etNewExerciseRepsName.setText("");
        etNewExerciseSetsName.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
