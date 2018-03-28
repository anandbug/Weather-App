package in.nishachar.anand.weather.home;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import java.util.List;

import in.nishachar.anand.weather.R;
import in.nishachar.anand.weather.models.LocationResponse;

/**
 * Created by anand on 13/03/18.
 */

class SearchListAdapter extends CursorAdapter implements Filterable {
    private List<LocationResponse> locationResponses;

    private AppCompatTextView nameText;

    SearchListAdapter(Context context, Cursor cursor, List<LocationResponse> locationResponses) {
        super(context, cursor, false);
        this.locationResponses = locationResponses;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        nameText.setText(locationResponses.get(cursor.getPosition()).getName());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_view_item, parent, false);
        nameText = view.findViewById(R.id.name);
        return view;
    }
}
