package com.example.a2020frcscouter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteCursor;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import static com.example.a2020frcscouter.TBAHandler.helper;

public class MainActivity extends FragmentActivity implements ListFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, AdapterView.OnItemSelectedListener {
    public static Context c; // Static context that can be accessed from other classes
    private static int currentMatch = 1;
    //public com.example.cameron.sql_testing.DatabaseContainer container = new DatabaseContainer(this);
    Button button;
    static TBAHandler handler;
    static ListView list;
    public static SharedPreferences sharedPref;
    public static String TBAKey;
    public static Spinner sortSpinner;
    public static ArrayAdapter<CharSequence> selectionAdapter;
    public static ArrayAdapter<JSONObject> listAdapter;
    public static RequestQueue queue;


//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment fragment;
//            Intent intent;
//            FragmentTransaction transaction;
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
////                    fragment = new ListFragment();
////                    transaction = getSupportFragmentManager().beginTransaction();
////                    transaction.replace(R.id.listFrameLayout, fragment);
////                    transaction.commit(); //TODO FUCK SHIT UP
//
//                    intent = getIntent();
//                    finish();
//                    startActivity(intent);
//
//                    //mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
//
//                    //mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    fragment = new SettingsFragment();
//                    transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.settingsFrameLayout, fragment);
//                    transaction.commit();
//                    //mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
//            return false;
//        }
//    };

    public static int getCurrentMatch() {
        return currentMatch;
    }

    public static void setCurrentMatch(int m) {
        currentMatch = m;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        c = getBaseContext();

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        list = findViewById(R.id.listMain);
        handler = new TBAHandler(this);

        TBAKey = sharedPref.getString(getString(R.string.settings_key_key), "yeet");
        queue = Volley.newRequestQueue(this);
        queue.start();

        Fragment fragment;
        FragmentTransaction transaction;

        handler.helper.onUpgrade(handler.helper.getWritableDatabase(), 0, 4);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 82; i++) {
                    handler.getMatchData(String.format("/match/%1$s_qm%2$d", "2019caoc", i));
                }

                DataHandler.printTeamsList();
//                FrodoCursorAdapter todoAdapter = new FrodoCursorAdapter(MainActivity.c, helper.getAllEntriesTeleopCursor(), "teleop");
//                MainActivity.list.setAdapter(todoAdapter);
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        sortSpinner = findViewById(R.id.sortSpinner);
        selectionAdapter = ArrayAdapter.createFromResource(this, R.array.sort_array, android.R.layout.simple_spinner_item);
        selectionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sortSpinner.setAdapter(selectionAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FrodoCursorAdapter todoAdapter;
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                switch(position) {
                    case 0:
                        //handler.getMatchData(String.format("/match/%1$s_qm%2$d", "2018mndu", currentMatch));
                        todoAdapter = new FrodoCursorAdapter(MainActivity.c, helper.getAllEntriesTeleopCursor(), "teleop");
                        MainActivity.list.setAdapter(todoAdapter);
                        //transaction.replace(R.id.listFrameLayout, new ListFragment());
                        //transaction.commit();
                        break;
                    case 1:
                        todoAdapter = new FrodoCursorAdapter(MainActivity.c, helper.getAllEntriesAutoCursor(), "auto");
                        MainActivity.list.setAdapter(todoAdapter);
                        break;
                    case 2:
                        todoAdapter = new FrodoCursorAdapter(MainActivity.c, helper.getAllEntriesHatchCursor(), "hatch");
                        MainActivity.list.setAdapter(todoAdapter);
                        break;
                    case 3:
                        todoAdapter = new FrodoCursorAdapter(MainActivity.c, helper.getAllEntriesCargoCursor(), "cargo");
                        MainActivity.list.setAdapter(todoAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                FrodoCursorAdapter todoAdapter = new FrodoCursorAdapter(MainActivity.c, helper.getAllEntriesTeamCursor(), "team");
                MainActivity.list.setAdapter(todoAdapter);
            }
        });

        MainActivity.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //(new MainActivity()).startTeamInfoFragment((SQLiteCursor) parent.getItemAtPosition(position));
                SQLiteCursor cursor = (SQLiteCursor) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.c, TeamInfoActivity.class);

                intent.putExtra("teamNum", Integer.toString(cursor.getInt(0)));
                intent.putExtra("teleop", Float.toString(cursor.getFloat(1)));
                intent.putExtra("cargoPoints", Float.toString(cursor.getFloat(cursor.getColumnIndex("cargoPoints"))));
                intent.putExtra("hatchPoints", Float.toString(cursor.getFloat(cursor.getColumnIndex("hatchPoints"))));
                intent.putExtra("autoPoints", Float.toString(cursor.getFloat(cursor.getColumnIndex("autoPoints"))));
                startActivity(intent);

            }
        });

        for (int i = 1; i < 82; i++) {
            handler.getMatchData(String.format("/match/%1$s_qm%2$d", "2019caoc", i));
        }

        /*FrodoCursorAdapter todoAdapter = new FrodoCursorAdapter(MainActivity.c, helper.getAllEntriesTeleopCursor(), "teleop");
        MainActivity.list.setAdapter(todoAdapter);*/

    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

}
