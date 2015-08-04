package com.sanca.catbrowser.networking;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanca on 04.08.15.
 */
public class JsonAuthorizedRequest extends JsonObjectRequest {

    Map<String, String> mExtraHeaders = null;

    public JsonAuthorizedRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        mExtraHeaders.putAll(headers);
        return mExtraHeaders;
    }

    public void addAuthorizationHeader(String userId, String password) {
        mExtraHeaders = new HashMap<String, String>();
        mExtraHeaders.put("Authorization", getB64Auth(userId, password));
    }

    private String getB64Auth (String login, String pass) {
        String source = login + ":" + pass;
        String ret = "Basic " + Base64.encodeToString(source.getBytes(),
                Base64.URL_SAFE | Base64.NO_WRAP);
        return ret;
    }
}
