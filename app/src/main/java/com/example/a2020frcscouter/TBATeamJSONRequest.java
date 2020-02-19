package com.example.a2020frcscouter;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TBATeamJSONRequest extends JsonObjectRequest {
    public TBATeamJSONRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener eListener) {
        super(method, url, jsonRequest, listener, eListener);
        //Log.d("minto", url);
    }

    @Override
    public Map getHeaders() throws AuthFailureError {
        HashMap headers = new HashMap();
        headers.put("X-TBA-Auth-Key", "YXwa7Fm6N0mks7XhnRMIVnJzsjE3frXe30GZAMp5r3rDmhdLFcZjsFkFTpxxRUtR");
        headers.put("User-Agent", "test-scout");

        return headers;
    }
}
