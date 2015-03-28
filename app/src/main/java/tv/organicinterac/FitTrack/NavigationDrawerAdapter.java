package tv.organicinterac.FitTrack;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Paul on 3/25/2015.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<String[]> {
    Context mContext;
    public NavigationDrawerAdapter(Context context, int resource, List<String[]> items) {
        super(context, resource, items);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        System.out.println(position);
        if (v == null) {
            LayoutInflater li;
            li = LayoutInflater.from(mContext);
            if (position == 0) {
                v = li.inflate(R.layout.nav_drawer_edit_list_item, null);
            } else if (position == 1) {
                v = li.inflate(R.layout.nav_drawer_send_list_item, null);
            }

        }
        return v;
    }
}
