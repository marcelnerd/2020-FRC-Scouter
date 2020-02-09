package com.example.a2020frcscouter;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class MainActivity extends FragmentActivity implements TeamInfoFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, AdapterView.OnItemSelectedListener, TeamListFragment.OnFragmentInteractionListener {
    public static SharedPreferences sharedPref;
    public static String TBAKey;

    public BottomNavigationView mainBottomNav;
    public static RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);

        mainBottomNav = findViewById(R.id.mainBottomNav);
        mainBottomNav.inflateMenu(R.menu.navigation);

        TBAKey = sharedPref.getString(getString(R.string.settings_key_key), "yeet");
        queue = Volley.newRequestQueue(this);
        queue.start();

        TBAKeysJSONRequest request = new TBAKeysJSONRequest(Request.Method.GET, "https://www.thebluealliance.com/api/v3/events/2019/simple", null, new TBAEventKeysListener(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("minto", "Json Request encountered error");
                error.printStackTrace();
                Log.v("minto", error.getMessage() + "");
            }

        });

        queue.add(request);

        TBAHandler.requestMatchKeys();

        mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) { //TODO Check the difference between getFragmentManger() and getSupportFragmentManager()
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }

                FragmentManager manager = getSupportFragmentManager(); // Might break
                FragmentTransaction transaction = manager.beginTransaction();

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

    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

}
