package com.example.a2020frcscouter;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity implements TeamInfoFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, AdapterView.OnItemSelectedListener, TeamListFragment.OnFragmentInteractionListener {
    public static SharedPreferences sharedPref;
    public static String TBAKey;

    public BottomNavigationView mainBottomNav;
    public static RequestQueue queue;

    public static int lastRecPosition = 0;
    public static Parcelable saveState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);

        mainBottomNav = findViewById(R.id.mainBottomNav);
        mainBottomNav.inflateMenu(R.menu.navigation);

        String eventName = sharedPref.getString("currentEventName", "yeet");
        if(eventName != null) {
            TBAHandler.setCurrentEvent(eventName);
        }

        queue = Volley.newRequestQueue(this);
        queue.start();

        int [][] states = new int [][]{
                new int[] { android.R.attr.state_enabled, -android.R.attr.state_checked},
                new int[] {android.R.attr.state_enabled, android.R.attr.state_checked}
        };

        //DataHandler.readSettingsFile();
        TBAHandler.requestMatchKeys();

        mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager manager = getSupportFragmentManager(); // Might break
                FragmentTransaction transaction = manager.beginTransaction();

                for (Fragment fragment : getSupportFragmentManager().getFragments()) { //TODO Check the difference between getFragmentManger() and getSupportFragmentManager()
                    transaction.remove(fragment);
                }

                switch(item.getTitle().toString()) {
                    case "List":
                        transaction.add(R.id.mainLayout, new TeamListFragment(), "teamListFraggy");
                        transaction.commit();
                        break;

                    case "Settings":
                        transaction.add(R.id.mainLayout, new SettingsFragment(), "settingsFraggy");
                        transaction.commit();
                }
                return false;
            }
        });

        FragmentManager manager = getSupportFragmentManager(); // Might break
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.mainLayout, new TeamListFragment(), "teamListFraggy");
        transaction.commit();

        if(saveState != null) {
            TeamListFragment.recMain.getLayoutManager().onRestoreInstanceState(saveState);
            saveState = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveState = TeamListFragment.recMain.getLayoutManager().onSaveInstanceState();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(saveState != null) {
            TeamListFragment.recMain.getLayoutManager().onRestoreInstanceState(saveState);
            saveState = null;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

}
