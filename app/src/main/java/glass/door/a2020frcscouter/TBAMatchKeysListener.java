package glass.door.a2020frcscouter;

import com.android.volley.Response;

import org.json.JSONArray;

public class TBAMatchKeysListener implements Response.Listener<JSONArray> {

    public TBAMatchKeysListener() {
    }


    @Override
    public void onResponse(JSONArray response) { //The response represents a single match
      // Log.d("minto", "Match Keys:     " + response.toString());
        TBAHandler.setMatchKeys(response);
    }
}