package com.example.a2020frcscouter;

import android.content.Context;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

public class TBAHandler {

    private static final String baseURL = "https://www.thebluealliance.com/api/v3";
    private static final String eventKey = "2019caoc"; //TODO DONT FORGET TO CHANGE THIS BACK
    private static final int currentYear = 2019;
    public static JSONObject json;
    //private RequestQueue queue;
    public static DBHelper helper; //This helper can be accessed statically by any class.


    public TBAHandler(Context context) {
        helper = new DBHelper(MainActivity.c);
        //queue = Volley.newRequestQueue(context);
        //queue.start();
    }

    public void getMatchData(String call) {
        TBAListener listener = new TBAListener(helper); // The helper is passed to the listner.
        HashMap[] map = null;
        TBAListener.yeet();

        String fullURL = baseURL + call;
        //String fullURL = baseURL + "/match/2018mndu_qm1";
        //Log.d("minto", fullURL);

        TBAJSONRequest request = new TBAJSONRequest(Request.Method.GET, fullURL, null, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            Log.d("minto", "Json Request encountered error");
            error.printStackTrace();
            Log.v("minto", error.getMessage() + "");
            }

        });

        MainActivity.queue.add(request);

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

    public static void setJson(JSONObject j) {
        json = j;
    }

}
