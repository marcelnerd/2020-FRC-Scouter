package glass.door.a2020frcscouter;

import android.util.Log;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class TBAHandler implements OnSwankListener {

    private static ArrayList<String> matchKeys = new ArrayList<>();
    private static HashMap eventKeys = new HashMap();
    private static ArrayList<String> eventNames = new ArrayList<>();
    public static int matchCounter = 1;
    public static final String baseURL = "https://www.thebluealliance.com/api/v3";
    private static final String eventKey = "2019caoc"; //TODO DON'T FORGET TO CHANGE THIS BACK
    private static String currentEventName;
    private static String currentEventKey;
    private final static int currentYear = 2020;
    public static JSONObject json;


    public TBAHandler() {

    }

    public static void getMatchData(String matchKey) {
        TBAMatchListener listener = new TBAMatchListener();

        ////VERY IMPORTANT//// Do not remove or everything will break probably
        TBAMatchListener.yeet();

        String fullURL = baseURL + "/match/" + matchKey;

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

    public static void requestMatchKeys() {
        if(TeamListFragment.refreshLayout != null) {
            TeamListFragment.refreshLayout.setRefreshing(true);
        }
        TBAMatchKeysListener listener = new TBAMatchKeysListener();
        String fullURL = baseURL + "/event/" + currentEventKey + "/matches/keys";


        TBAKeysJSONRequest request = new TBAKeysJSONRequest(Request.Method.GET, fullURL, null, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("minto", "Json Request encountered error");
                error.printStackTrace();
                Log.v("minto", error.getMessage() + "");
            }

        });

        Log.d("minto", fullURL);
        MainActivity.queue.add(request);

    }

    public static void setMatchKeys(JSONArray keyArray) {
        DataHandler.clearTeams();
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

    public static void setEvents(JSONArray eventArray) {
        try {
            JSONObject tempO;
            eventKeys.clear();
            for (int i = 0; i < eventArray.length(); i++) {
                tempO = eventArray.getJSONObject(i);
                eventKeys.put(tempO.getString("name"), tempO.get("key"));
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        //Log.v("minto", eventKeys.toString());

        eventNames.clear();
        eventNames.add("");
        for(Object name : eventKeys.keySet()) {
            eventNames.add(name.toString());
        }

//        Log.d("minto", "HERERER");
//        currentEventKey = null;
//        currentEventName = null;
        Collections.sort(eventNames);
        Log.d("minto", "aaaa" + eventNames.toString());
        //requestMatchKeys();
    }

    public static void setCurrentEvent(String name) {
        currentEventName = name;
        currentEventKey = (String) eventKeys.get(name);
    }

    public static void setCurrentEventKey(String key) {
        currentEventKey = key;
    }

    public static int getCurrentEventIndex() {
        return eventNames.indexOf(currentEventName);
    }

    public static String getCurrentEventName() {
        return currentEventName;
    }

    public static String getCurrentEventKey() { return currentEventKey;}

    public static ArrayList<String> getEventNames() {
        return eventNames;
    }

    public static int getMatchCounter() {
        return matchCounter;
    }

    public static ArrayList<String> getMatchKeys() {
        return matchKeys;
    }

    public static HashMap getEventKeys() {
        return eventKeys;
    }

//    public static void setCurrentYear(int y) { // !!!!!!!!!!!NONONONONONONONONONONONONONO NO!!!!!!!!!!!!!!!!!!!
//        currentYear = y;
//
//        TBAKeysJSONRequest request = new TBAKeysJSONRequest(Request.Method.GET, "https://www.thebluealliance.com/api/v3/events/" + currentYear + "/simple", null, new TBAEventKeysListener(), new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("minto", "Json Request encountered error");
//                error.printStackTrace();
//                Log.v("minto", error.getMessage() + "");
//            }
//
//        });
//
//        MainActivity.queue.add(request);
//    }

    public static int getCurrentYear() {
        return currentYear;
    }

    @Override
    public void OnSwank(TeamJSONObject team) {
    }

}
