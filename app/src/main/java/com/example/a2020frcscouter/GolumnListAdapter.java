package com.example.a2020frcscouter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GolumnListAdapter extends ArrayAdapter<JSONObject> {
    String sortOption;
    ArrayList<JSONObject> list;

    public GolumnListAdapter(Context context, int resource, ArrayList<JSONObject> l, String s) {
        super(context, resource, l);
        sortOption = s;
        list = l;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject team = getItem(position); // Get the data item for this position
        JSONArray statList; // The JSONArray of score values from the team
        double average = 0; // The average of the score values that will be displayed


        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.team_entry_golumn, parent, false); //TODO Make layout file for a golumn team entry
        }
        // Lookup view for data population
        TextView teamNumText = (TextView) convertView.findViewById(R.id.TeamNumText);
        TextView statText = (TextView) convertView.findViewById(R.id.statText);

        // Populate the data into the template view using the data object
        try {
           // JSONObject tempTeam = list.get(position);

            teamNumText.setText(team.getString("teamNum"));

            for(String s : DataHandler.scoreKeys) {
                if(sortOption.equals(s)) {
                    statList = team.getJSONArray(s);
                    for(int i = 0; i < statList.length(); i++) {
                        average += statList.getInt(i);
                    }
                    average /= statList.length();
                    break;
                }
            }

            statText.setText(Double.toString(average));
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
