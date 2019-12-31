package com.example.a2020frcscouter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class FrodoCursorAdapter extends CursorAdapter {

    String sortOption;

    public FrodoCursorAdapter(Context context, Cursor cursor, String s) {
        super(context, cursor, 0);
        sortOption = s;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.team_entry_frodo, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView teamNumText = view.findViewById(R.id.TeamNumText);
        TextView statText = view.findViewById(R.id.statText);

        teamNumText.setText(Integer.toString(cursor.getInt(0)));

        switch(sortOption) {
            case "teleop":
                statText.setText(cursor.getString(cursor.getColumnIndex("teleopPoints")).toString());
                break;
            case "auto":
                statText.setText(cursor.getString(cursor.getColumnIndex("autoPoints")).toString());
                break;
            case "hatch":
                statText.setText(cursor.getString(cursor.getColumnIndex("hatchPoints")).toString());
            case "cargo":
                statText.setText(cursor.getString(cursor.getColumnIndex("cargoPoints")).toString());

        }

        /*telePointsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.list.setAdapter(new FrodoCursorAdapter(MainActivity.c, TBAHandler.helper.getAllEntriesTeleopCursor(), sortOption));
                Log.v("minto", "clicked thing");
            }
        });*/

        /*
        // Extract properties from cursor
        String teamNum = cursor.getString(cursor.getColumnIndex("_id")).toString();
        String teleopPoints = cursor.getString(cursor.getColumnIndex("teleopPoints")).toString();
        String autoPoints = cursor.getString(cursor.getColumnIndex("autoPoints")).toString();
        String vaultPoints = cursor.getString(cursor.getColumnIndex("vaultPoints")).toString();
        String climb = new String((Double.parseDouble(cursor.getString(cursor.getColumnIndex("climb")))*100) + "%");
        String autoRun = new String((Double.parseDouble(cursor.getString(cursor.getColumnIndex("autoRun")))*100) + "%");
        String matchesPlayed = cursor.getString(cursor.getColumnIndex("matchesPlayed")).toString();

        // Populate fields with extracted properties
        teamNumText.setText(teamNum);
        telePointsText.setText(teleopPoints);
        autoPointsText.setText(autoPoints);
        climbText.setText(climb);
        vaultPointsText.setText(vaultPoints);
        autoRunText.setText(autoRun);
        matchesText.setText(matchesPlayed);
        */
    }
}
