package glass.door.a2020frcscouter;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class TBAMatchListener implements Response.Listener<JSONObject> {

    public TBAMatchListener() {
    }


    @Override
    public void onResponse(JSONObject response) { //The response represents a single match

//        try {
//            map = JSONHandler2019.getMatchData(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.v("minto", "you fucked up; its the getMatchData thing");
//
//        }
//
//
//        try {
//        } catch(Exception e) {
//            //fuck you
//        }

       // Log.d("minto", "Matches:      " + response.toString());

        DataHandler.update(response);

        if(TBAHandler.getMatchCounter() == TBAHandler.getMatchKeys().size()) {
            TBAHandler.setMatchCounter(1);
            MainActivity.queue.add(new TBAJSONRequest(Request.Method.GET, TBAHandler.baseURL + "/event/" + TBAHandler.getCurrentEventKey() + "/rankings", null, new TeamRanksListener(), new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("minto", "JSON Request encountered error");
                    error.printStackTrace();
                    Log.v("minto", error.getMessage() + "");
                }
            }));

            //TeamListFragment.recMain.getRecycledViewPool().clear();
            //TeamListFragment.mAdapter.notifyDataSetChanged();

            //Log.d("minto", TBAHandler.getMatchCounter() + "     " + TBAHandler.getMatchKeys().size());
            //DataHandler.printTeamsList();
        }
        else {
            TBAHandler.setMatchCounter(TBAHandler.getMatchCounter() + 1);
        }


//        if(MainActivity.getCurrentMatch() == 81) { //TODO Here is the "printTeamsList()" call
//            DataHandler.printTeamsList();
//        }

//        for (HashMap e : map) {
//            Log.v("minto", e.toString());
//
//           // helper.updateTeamStats(team);
//        }


    }

    public static void yeet() {
    }
}


