package glass.door.a2020frcscouter;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Response;

import org.json.JSONObject;

public class TBATeamListener implements Response.Listener<JSONObject> {
    JSONObject scoresJSON;
    Context context;

    public TBATeamListener(TeamJSONObject t, Context c) {
        scoresJSON = t;
        context = c;
    }

    @Override
    public void onResponse(JSONObject json) {
        Intent intent = new Intent(MyAppy.getAppContext(), TeamInfoActivity.class);

        if(scoresJSON != null) {
            intent.putExtra("scoresJson", scoresJSON.toString());
        }
        else {
            intent.putExtra("scoresJson", "nope");
        }


        intent.putExtra("teamJson", json.toString());
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
