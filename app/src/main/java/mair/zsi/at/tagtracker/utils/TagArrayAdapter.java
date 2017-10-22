package mair.zsi.at.tagtracker.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mair.zsi.at.tagtracker.R;
import mair.zsi.at.tagtracker.entities.Tag;

/**
 * Created by sebastian on 21.10.17.
 */

public class TagArrayAdapter extends ArrayAdapter<Tag> {
    public TagArrayAdapter(Context context, List<Tag> tags) {
        super(context, 0, tags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Tag tag = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tag, parent, false);simple_list_item_1
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(android.R.id.text1);
        // Populate the data into the template view using the data object
        tvName.setText(tag.getName());
        // Return the completed view to render on screen
        return convertView;
    }
}

