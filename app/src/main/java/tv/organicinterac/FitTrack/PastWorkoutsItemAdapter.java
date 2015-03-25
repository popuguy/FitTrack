package tv.organicinterac.FitTrack;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 3/24/2015.
 */
public class PastWorkoutsItemAdapter extends ArrayAdapter<String[]> {
    Context mContext;
    public PastWorkoutsItemAdapter(Context context, int resource, List<String[]> items) {
        super(context, resource, items);
        mContext = context;
    }
    TextView tvTitle;
    TextView tvDate;
    ListView lvPastExercises;
    InnerCardItemAdapter adapter;
    LinearLayout llExercisesLayout;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.past_workouts_list_item, null);
        }
        String[] p = getItem(position);
        /* needs to have name, date, item #1, item #2, item #3 */
        if (p.length == 5) {

            Toolbar toolbar = (Toolbar) v.findViewById(R.id.card_toolbar);
            if (toolbar != null) {
                tvTitle = (TextView) v.findViewById(R.id.title_textview);
//                toolbar.setTitle("Card Toolbar");
                tvTitle.setText(p[0]);
                tvDate = (TextView) v.findViewById(R.id.date_textview);
                tvDate.setText(p[1]);

                ArrayList<String[]> items = new ArrayList<>();
                items.add(new String[]{"Bentover row"});
                items.add(new String[]{"Stiff-legged deadlift"});
                items.add(new String[]{"Squat"});

                adapter = new InnerCardItemAdapter(mContext, R.layout.mini_past_workout_exercise_list_item, items);

                lvPastExercises = (ListView) v.findViewById(R.id.past_exercises_listview);
                lvPastExercises.setAdapter(adapter);
//
//                int numItems = lvPastExercises.getCount();
//                int firstVisible = lvPastExercises.getFirstVisiblePosition();
//                View firstItem = lvPastExercises.getChildAt(firstVisible);
////                int itemHeight = firstItem.getHeight();
//                lvPastExercises.setMinimumHeight(600);
//                lvPastExercises.setMinimumHeight(
//                        lvPastExercises.getCount() *
//                                lvPastExercises.getChildAt(0).getHeight()
//                );


                // inflate your menu
                toolbar.inflateMenu(R.menu.card_menu);
                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        return true;
                    }
                });
            }
        }
        return v;
    }
}
