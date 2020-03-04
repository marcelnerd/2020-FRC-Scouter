package glass.door.a2020frcscouter;

import android.provider.Settings;
import android.widget.ArrayAdapter;

import com.android.volley.Response;

import org.json.JSONArray;

public class TBAEventKeysListener implements Response.Listener<JSONArray> {

    public TBAEventKeysListener() {
    }


    @Override
    public void onResponse(JSONArray response) { //The response represents a single match
        TBAHandler.setEvents(response);
        if(SettingsFragment.eventSpinner != null) {
            SettingsFragment.eventSpinner.setAdapter(new ArrayAdapter<>(MyAppy.getAppContext(), android.R.layout.simple_spinner_item, TBAHandler.getEventNames()));
        }
    }
}
