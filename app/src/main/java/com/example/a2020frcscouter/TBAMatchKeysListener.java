package com.example.a2020frcscouter;

import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TBAMatchKeysListener implements Response.Listener<JSONArray> {

    public TBAMatchKeysListener() {
    }


    @Override
    public void onResponse(JSONArray response) { //The response represents a single match
      // Log.d("minto", "Match Keys:     " + response.toString());
        TBAHandler.setMatchKeys(response);
    }
}