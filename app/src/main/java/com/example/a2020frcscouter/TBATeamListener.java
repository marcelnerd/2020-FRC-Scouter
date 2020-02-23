package com.example.a2020frcscouter;

import android.content.Intent;

import com.android.volley.Response;

import org.json.JSONObject;

public class TBATeamListener implements Response.Listener<JSONObject> {
    JSONObject scoresJSON;

    public TBATeamListener(TeamJSONObject t) {
        scoresJSON = t;
    }

    @Override
    public void onResponse(JSONObject json) {
        Intent intent = new Intent(MyAppy.getAppContext(), TeamInfoActivity.class);
        intent.putExtra("scoresJson", scoresJSON.toString());
        intent.putExtra("teamJson", json.toString());
        MyAppy.getAppContext().startActivity(intent);
    }
}
