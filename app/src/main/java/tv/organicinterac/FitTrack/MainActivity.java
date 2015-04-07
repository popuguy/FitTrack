package tv.organicinterac.FitTrack;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity {

    PastWorkoutsItemAdapter adapter;
    NavigationDrawerAdapter mNavBarAdapter;
    ListView lvPastWorkouts;

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mStartWorkout;

    public static final int MAX_COMPLETE_EXERCISES_TO_DISPLAY_ON_CARD = 3;
    public static final String DATETIME_DATABASE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_VISIBLE_FORMAT = "MM/dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupPastWorkoutsItemAdapter();


        setupDrawerLayout();
        addDrawerItems();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mStartWorkout = (TextView) findViewById(R.id.start_workout_textview);
        mStartWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Let's start to work out!");
            }
        });



    }

    private void setupPastWorkoutsItemAdapter() {
        DatabaseInteraction db = new DatabaseInteraction(this);
        ArrayList<String[]> items = new ArrayList<>();
        System.out.println("inside of setupPastWorkoutsItemAdapter()");
        List <String[]> completeWorkouts = db.getCompleteWorkouts();
        for (String[] completeWorkout: completeWorkouts) {
            ArrayList<String> item = new ArrayList<>();

            List<String[]> completeExercises = db.getCompleteExercisesByCompleteWorkoutId(
                    Long.parseLong(completeWorkout[0]));

            item.add(completeWorkout[0]);

            SimpleDateFormat sdfDatabase = new SimpleDateFormat(DATETIME_DATABASE_FORMAT);
            SimpleDateFormat sdfVisible = new SimpleDateFormat(DATETIME_VISIBLE_FORMAT);

            Date workoutDate = null;
            try {
                workoutDate = sdfDatabase.parse(completeWorkout[2]);
                item.add(sdfVisible.format(workoutDate));
            } catch (ParseException e) {
                item.add("DATE ERROR");
            }

            int numAdded = 0;
            for (String[] completeExercise: completeExercises) {
                if (numAdded >= MAX_COMPLETE_EXERCISES_TO_DISPLAY_ON_CARD)
                    break;
                item.add(completeExercise[0]);
                numAdded += 1;
            }
            items.add(item.toArray(new String[item.size()]));
        }





        adapter = new PastWorkoutsItemAdapter(this, R.layout.past_workouts_list_item, items);

        lvPastWorkouts = (ListView) findViewById(R.id.past_workouts_listview);
        lvPastWorkouts.setAdapter(adapter);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    private void addDrawerItems() {
        String[] osArray = { "Edit Workouts", "Send Feedback" };
        ArrayList<String[]> list = new ArrayList<>();
        list.add(new String[]{});
        list.add(new String[]{});
        mNavBarAdapter = new NavigationDrawerAdapter(this, 0, list);
        mDrawerList.setAdapter(mNavBarAdapter);
    }
//private void addDrawerItems() {
//    String[] osArray = { "Android", "iOS", "Windows", "OS X", "Linux" };
//    mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
//    mDrawerList.setAdapter(mAdapter);
//}

    private void setupDrawerLayout() {
        mDrawerList = (ListView)findViewById(R.id.navList);mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (id) {
//            case R.id.action_new_workout:
//                toast("Let's make a new workout!");
//                return true;
//            case R.id.action_start_workout:
//                toast("Time to start a workout");
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toast(String out) {
        //cuz I'm real lazy like that
        Toast.makeText(this, out, Toast.LENGTH_SHORT).show();
    }
}
