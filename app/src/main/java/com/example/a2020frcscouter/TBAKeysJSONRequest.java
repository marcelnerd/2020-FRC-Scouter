package com.example.a2020frcscouter;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TBAKeysJSONRequest extends JsonArrayRequest {

    public TBAKeysJSONRequest(int method, String url, JSONArray jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener eListener) {
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
