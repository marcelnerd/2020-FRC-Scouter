package com.example.a2020frcscouter;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TBAMatchListener implements Response.Listener<JSONObject> {

    public TBAMatchListener() {
    }


    @Override
    public void onResponse(JSONObject response) { //The response represents a single match

//        try {
//            map = JSONHandler2019.getMatchData(response);
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

       // Log.d("minto", "Matches:      " + response.toString());

        DataHandler.update(response);

        if(TBAHandler.getMatchCounter() == TBAHandler.getMatchKeys().size()) {
            TBAHandler.setMatchCounter(1);
            //TeamListFragment.list.setAdapter(TeamListFragment.getSelectedAdapter(TeamListFragment.sortSpinner.getSelectedItemPosition()));
            TeamListFragment.recMain.setAdapter(new GolumnRecyleAdapter("teleopPoints"));
            TeamListFragment.refreshLayout.setRefreshing(false);

            //Log.d("minto", TBAHandler.getMatchCounter() + "     " + TBAHandler.getMatchKeys().size());
            DataHandler.printTeamsList();
        }
        else {
            TBAHandler.setMatchCounter(TBAHandler.getMatchCounter() + 1);
        }


//        if(MainActivity.getCurrentMatch() == 81) { //TODO Here is the "printTeamsList()" call
//            DataHandler.printTeamsList();
//        }

//        for (HashMap e : map) {
//            Log.v("minto", e.toString());
//
//           // helper.updateTeamStats(team);
//        }


    }

    public static void yeet() {
    }
}


