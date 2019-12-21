package com.example.a2020frcscouter;

import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TBAListener implements Response.Listener<JSONObject> {

    DBHelper helper;

    public TBAListener(DBHelper h) {
        //Log.d("minto", "CREATED LISTENER");
        helper = h;
    }


    @Override
    public void onResponse(JSONObject response) {
        HashMap[] map = null;
        FRC2019Team team = null;

        try {
            map = JSONHandler.getMatchData(response);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("minto", "you fucked up; its the getMatchData thing");

        }


        try {
        } catch(Exception e) {
            //fuck you
        }

        MainActivity.setCurrentMatch((MainActivity.getCurrentMatch() + 1)); //TODO Redo how you keep track of match numbers
        for (HashMap e : map) {
            Log.v("minto", e.toString());

            team = FRC2019Team.buildTeam(e.entrySet());
            //TODO Add team to data
            helper.updateTeamStats(team);
        }

    }

    public static void yeet() {
    }
}


