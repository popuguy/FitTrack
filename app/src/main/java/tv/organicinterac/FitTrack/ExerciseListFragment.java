package tv.organicinterac.FitTrack;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by Paul on 3/17/2015.
 */
public class ExerciseListFragment extends Fragment implements View.OnClickListener {

    Button bAdd;
    ImageButton bStart;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflated = inflater.inflate(R.layout.activity_main, container, false);

        bStart = (ImageButton) inflated.findViewById(R.id.start_button);
        bAdd = (Button) inflated.findViewById(R.id.button);
        tvTimer = (TextView) inflated.findViewById(R.id.timer_textview);
        origText = tvTimer.getText().toString();
        lvWorkoutItems = (ListView) inflated.findViewById(R.id.workout_items_listview);
        rlNewEvent = (RelativeLayout) inflated.findViewById(R.id.new_event_relativelayout);
        etNewExerciseName = (EditText) inflated.findViewById(R.id.new_exercise_name_edittext);
        etNewExerciseSetsName = (EditText) inflated.findViewById(R.id.new_exercise_sets_edittext);
        etNewExerciseRepsName = (EditText) inflated.findViewById(R.id.new_exercise_reps_edittext);
        ArrayList<String[]> items = new ArrayList<>();
        adapter = new WorkoutAdapter(inflated.getContext());
        rawItems = adapter.getExercises();
        for (String[] rawItem: rawItems) {
            items.add(new String[]{rawItem[1], rawItem[2]});
        }
        wia = new WorkoutItemAdapter(inflated.getContext(), R.layout.list_item, items);

        lvWorkoutItems.setAdapter(wia);


        bAdd.setOnClickListener(this);
        bStart.setOnClickListener(this);

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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvTimer.setText(String.format("Current time: %02d:%02d:%02d",
                                    diffHour, diffMin, diffSec));
                        }
                    });
                }
            }
        }, 1000, 1000);
        return inflated;
    }

    public void toggleTimer() {
        if (running) {
            running = false;
        } else {
            startRun = new Date();
            running = true;
        }
    }
    private void addExercise() {
        if (rlNewEvent.getVisibility() != View.VISIBLE) {
            rlNewEvent.setVisibility(View.VISIBLE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(rlNewEvent, "alpha", 0f, 1f);
            animator.setDuration(550);
            animator.start();
        } else {
            String[] newExercise = getNewExercise();
            if (newExercise[0].length() > 0 && newExercise[1].length() > 4){
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etNewExerciseRepsName.getWindowToken(), 0);
                wia.add(newExercise);
                adapter.addExercise(newExercise[0], newExercise[1]);
                clearExerciseEntry();
                ObjectAnimator animator = ObjectAnimator.ofFloat(
                        rlNewEvent, "alpha", 1f, 0f);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_button:
                toggleTimer();
                break;
            case R.id.button:
                addExercise();
                break;
        }
    }
    private void toast(String out) {
        //cuz I'm real lazy like that
        Toast.makeText(getActivity().getApplicationContext(), out, Toast.LENGTH_SHORT).show();
    }
}
