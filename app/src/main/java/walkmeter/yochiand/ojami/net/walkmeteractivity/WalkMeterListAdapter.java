package walkmeter.yochiand.ojami.net.walkmeteractivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by momomo00 on 15/02/06.
 */
public class WalkMeterListAdapter
    extends ArrayAdapter<WalkMeterData> {

    private ArrayList<WalkMeterData> mItems;
    private LayoutInflater mInflater;

    public WalkMeterListAdapter(Context context, int viewResourceID, ArrayList<WalkMeterData> items) {
        super(context, viewResourceID, items);

        mItems = items;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = mInflater.inflate(R.layout.list_row, null);
        }

        WalkMeterData data = mItems.get(position);
        TextView tv = (TextView)view.findViewById(R.id.history_row);
        tv.setText(data.StartDate);

        return view;
    }
}
