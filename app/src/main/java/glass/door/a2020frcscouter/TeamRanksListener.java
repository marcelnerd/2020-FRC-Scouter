package glass.door.a2020frcscouter;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TeamRanksListener implements Response.Listener<JSONObject> {
    public TeamRanksListener() {

    }

    @Override
    public void onResponse(JSONObject response) { //The response represents a single match
        try {
            JSONObject teamRankObject = response.getJSONArray("rankings").getJSONObject(0);
            Log.d("minto", "Team: " + teamRankObject.getString("team_key") + "  Rank: " + teamRankObject.getString("rank"));
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        DataHandler.updateRanks(response);
        TeamListFragment.mAdapter.updateTeamList();
        TeamListFragment.refreshLayout.setRefreshing(false);
    }
}
