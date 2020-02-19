package com.example.a2020frcscouter;

import org.json.JSONException;
import org.json.JSONObject;

public class TeamJSONObject extends JSONObject {
    boolean expanded;

    public TeamJSONObject() {
        super();
    }

    public TeamJSONObject(String jsonString) throws JSONException {
        super(jsonString);
        expanded = false;
    }

    public boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(boolean b) {
        expanded = b;
    }
}
