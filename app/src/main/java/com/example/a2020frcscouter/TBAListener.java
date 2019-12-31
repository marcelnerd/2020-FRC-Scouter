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
    public void onResponse(JSONObject response) { //The response represents a single match
        HashMap[] map = null;
        FRC2019Team team = null;

//        try {
//            map = JSONHandler.getMatchData(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.v("minto", "you fucked up; its the getMatchData thing");
//
//        }
//
//
//        try {
//        } catch(Exception e) {
//            //fuck you
//        }

        MainActivity.setCurrentMatch((MainActivity.getCurrentMatch() + 1)); //TODO Redo how you keep track of match numbers
        DataHandler.update(response);

        if(MainActivity.getCurrentMatch() == 81) {
            DataHandler.printTeamsList();
        }

//        for (HashMap e : map) {
//            Log.v("minto", e.toString());
//
//           // helper.updateTeamStats(team);
//        }


    }

    public static void yeet() {
    }
}


