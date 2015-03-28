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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("executed onCreate!!!!!");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        DatabaseInteraction db = new DatabaseInteraction(this);

        ArrayList<String[]> items = new ArrayList<>();
        items.add(new String[]{"Casey Butt plan A","5/10","Bentover row","Squats","Bench press"});
        items.add(new String[]{"Casey Butt plan B","5/12","Front squats","Incline bench press","Pull-ups"});
//        items.add(new String[]{"Casey Butt plan B","5/12","","",""});
        adapter = new PastWorkoutsItemAdapter(this, R.layout.past_workouts_list_item, items);

        lvPastWorkouts = (ListView) findViewById(R.id.past_workouts_listview);
        lvPastWorkouts.setAdapter(adapter);

        setupDrawerLayout();
        addDrawerItems();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
