package com.example.a2020frcscouter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TBAHandler {

    private static ArrayList<String> matchKeys = new ArrayList<>();
    public static int matchCounter = 1;
    private static final String baseURL = "https://www.thebluealliance.com/api/v3";
    private static final String eventKey = "2019caoc"; //TODO DON'T FORGET TO CHANGE THIS BACK
    private static String currentEventKey = "2019caoc";
    private static final int currentYear = 2019;
    public static JSONObject json;


    public TBAHandler(Context context) {

    }

    public static void getMatchData(String matchKey) {
        TBAMatchListener listener = new TBAMatchListener(); // The helper is passed to the listner.

        ////VERY IMPORTANT//// Do not remove or everything will break probably
        TBAMatchListener.yeet();

        String fullURL = baseURL + "/match/" + matchKey;

        TBAMatchJSONRequest request = new TBAMatchJSONRequest(Request.Method.GET, fullURL, null, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            Log.d("minto", "Json Request encountered error");
            error.printStackTrace();
            Log.v("minto", error.getMessage() + "");
            }

        });

        MainActivity.queue.add(request);

    }

    public static void requestMatchKeys() {
        TBAMatchKeysListener listener = new TBAMatchKeysListener();
        String fullURL = baseURL + "/event/" + currentEventKey + "/matches/keys";


        TBAMatchKeysJSONRequest request = new TBAMatchKeysJSONRequest(Request.Method.GET, fullURL, null, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("minto", "Json Request encountered error");
                error.printStackTrace();
                Log.v("minto", error.getMessage() + "");
            }

        });

        Log.d("minto", "happeneed");
        MainActivity.queue.add(request);

    }

    public static void setMatchKeys(JSONArray keyArray) {
        matchKeys.clear();

        try {
            for (int i = 0; i < keyArray.length(); i++) {
                matchKeys.add(keyArray.getString(i));
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
            Log.d("minto", "Problem parsing match keys from jsonArray");
        }

        for(String match : matchKeys) {
            getMatchData(match);
        }
    }

    public static void setMatchCounter(int c) {
        matchCounter = c;
        if(matchCounter == TBAHandler.getMatchKeys().size()) {
        }
    }

    public static int getMatchCounter() {
        return matchCounter;
    }

    public void setCurrentEventKey(String key) {
        currentEventKey = key;
    }

    public static ArrayList<String> getMatchKeys() {
        return matchKeys;
    }

    public static String getMatch(int matchNum) {
        return String.format("/match/%1$s_qm%2$d", eventKey, matchNum);
    }

    public static String getTeamStatus(int teamNumber) {
        return String.format("/team/frc%1$d/events/%2$d/statuses", teamNumber, currentYear);
    }

    public static String getTeamMatches(int teamNumber) {
        return String.format("/team/frc%1$d/event/%2$s/matches", teamNumber, eventKey);
    }

}
