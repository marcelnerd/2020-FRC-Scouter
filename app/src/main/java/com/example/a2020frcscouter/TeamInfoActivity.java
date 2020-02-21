package com.example.a2020frcscouter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONException;

public class TeamInfoActivity extends AppCompatActivity {
    TeamJSONObject teamJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);
        try {
            teamJson = new TeamJSONObject(getIntent().getExtras().getString("team json"));
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }

}
