package com.example.a2020frcscouter;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity implements TeamInfoFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, AdapterView.OnItemSelectedListener, TeamListFragment.OnFragmentInteractionListener {
    public static SharedPreferences sharedPref;
    public static String TBAKey;

    //public BottomNavigationView mainBottomNav;
    public static RequestQueue queue;

    public static int lastRecPosition = 0;
    public static Parcelable saveState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("An Interesting Title");
        getSupportActionBar().setSubtitle("An Interestinger Subtitle");

        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.gandalf_toolbar, null);
        EditText searchText = view.findViewById(R.id.gandalf_search);
        searchText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    int num = Integer.parseInt(v.getText().toString());
                    if(DataHandler.teamIsAtSelectedEvent(num)) {
                        MainActivity.queue.add(new TBAJSONRequest(Request.Method.GET, TBAHandler.baseURL + "/team/frc" + num, null, new TBATeamListener(DataHandler.getTeamJSON(num)), new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("minto", "JSON Request encountered error");
                                error.printStackTrace();
                                Log.v("minto", error.getMessage() + "");
                            }
                        }));
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Unable to find team with that number at the selected event", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
                return false;
            }
        });

        ImageButton settingsButton = view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager(); // Might break
                FragmentTransaction transaction = manager.beginTransaction();

                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    transaction.remove(fragment);
                }
                transaction.add(R.id.mainLayout, new SettingsFragment(), "settingsFraggy");
                transaction.commit();
            }
        });

        getSupportActionBar().setCustomView(view);

        getSupportActionBar().setDisplayShowCustomEnabled(true);

        //mainBottomNav = findViewById(R.id.mainBottomNav);
        //mainBottomNav.inflateMenu(R.menu.navigation);

        String eventName = sharedPref.getString("currentEventName", "yeet");
        String eventKey = sharedPref.getString("currentEventKey", "yoink");
        if(eventName != null && eventKey != null) {
            Log.d("minto", "Creation event: " + eventName + ", " + eventKey);
            TBAHandler.setCurrentEvent(eventName);
            TBAHandler.setCurrentEventKey(eventKey);
        }

        queue = Volley.newRequestQueue(this);
        queue.start();

        int [][] states = new int [][]{
                new int[] { android.R.attr.state_enabled, -android.R.attr.state_checked},
                new int[] {android.R.attr.state_enabled, android.R.attr.state_checked}
        };

        //DataHandler.readSettingsFile();
        TBAHandler.requestMatchKeys();

        TBAKeysJSONRequest request = new TBAKeysJSONRequest(Request.Method.GET, "https://www.thebluealliance.com/api/v3/events/2020/simple", null, new TBAEventKeysListener(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("minto", "Json Request encountered error");
                error.printStackTrace();
                Log.v("minto", error.getMessage() + "");
            }

        });

        MainActivity.queue.add(request);

//        mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                FragmentManager manager = getSupportFragmentManager(); // Might break
//                FragmentTransaction transaction = manager.beginTransaction();
//
//                for (Fragment fragment : getSupportFragmentManager().getFragments()) { //TODO Check the difference between getFragmentManger() and getSupportFragmentManager()
//                    transaction.remove(fragment);
//                }
//
//                switch(item.getTitle().toString()) {
//                    case "List":
//                        transaction.add(R.id.mainLayout, new TeamListFragment(), "teamListFraggy");
//                        transaction.commit();
//                        break;
//
//                    case "Settings":
//                        transaction.add(R.id.mainLayout, new SettingsFragment(), "settingsFraggy");
//                        transaction.commit();
//                }
//                return false;
//            }
//        });

        FragmentManager manager = getSupportFragmentManager(); // Might break
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.mainLayout, new TeamListFragment(), "teamListFraggy");
        transaction.commit();

        if(saveState != null) {
            TeamListFragment.recMain.getLayoutManager().onRestoreInstanceState(saveState);
            saveState = null;
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if(item.getTitle().equals("Settings")) {
//            FragmentManager manager = getSupportFragmentManager(); // Might break
//            FragmentTransaction transaction = manager.beginTransaction();
//
//            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
//                transaction.remove(fragment);
//            }
//            transaction.add(R.id.mainLayout, new SettingsFragment(), "settingsFraggy");
//            transaction.commit();
//
//            // Again
//
////            FragmentManager fmanager = getSupportFragmentManager(); // Might break
////            FragmentTransaction ftransaction = fmanager.beginTransaction();
////
////            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
////                ftransaction.remove(fragment);
////            }
////            ftransaction.add(R.id.mainLayout, new SettingsFragment(), "settingsFraggy");
////            ftransaction.commit();
//        }
//        else if(item.getTitle().equals("List")) {
//            FragmentManager manager = getSupportFragmentManager(); // Might break
//            FragmentTransaction transaction = manager.beginTransaction();
//
//            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
//                transaction.remove(fragment);
//            }
//            transaction.add(R.id.mainLayout, new TeamListFragment(), "listFraggy");
//            transaction.commit();
//        }
//
//        //noinspection SimplifiableIfStatement
//        Toast.makeText(MainActivity.this, "Epstein Didn't Kill Himself", Toast.LENGTH_LONG).show();
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.navigation, menu);
//        return true;
//    }

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
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Fragment currentFrag = getSupportFragmentManager().findFragmentByTag("settingsFraggy");
            if(currentFrag != null && currentFrag.isVisible()) {
                FragmentManager manager = getSupportFragmentManager(); // Might break
                FragmentTransaction transaction = manager.beginTransaction();

                for (Fragment fragment : getSupportFragmentManager().getFragments()) { //TODO Check the difference between getFragmentManger() and getSupportFragmentManager()
                    transaction.remove(fragment);
                }
                transaction.add(R.id.mainLayout, new TeamListFragment(), "listFraggy");
                transaction.commit();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

}
