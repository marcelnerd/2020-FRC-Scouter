package com.example.a2020frcscouter;

import com.android.volley.Response;

import org.json.JSONArray;

public class TBAEventKeysListener implements Response.Listener<JSONArray> {

    public TBAEventKeysListener() {
    }


    @Override
    public void onResponse(JSONArray response) { //The response represents a single match
        TBAHandler.setEvents(response);
    }
}
