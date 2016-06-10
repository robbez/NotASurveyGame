package com.crippledcomputerboyz.robbez.notasurveygame.ExternalCommunication;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.crippledcomputerboyz.robbez.notasurveygame.GameLogic.GameDecision;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by robbez on 5/23/16.
 */
public class Logger {

    private RequestQueue mRequestQueue;
    private String url;
    private Context mContext;

    public Logger(Context context) {
        this.mContext = context;
        this.mRequestQueue = Volley.newRequestQueue(context);
        this.url = "http://crippled-63750.onmodulus.net/newgamedata";
    }

    public void logDecisionSet(ArrayList<GameDecision> decisions, String userName) {
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray.put(new JSONObject("{\"username\":\"" + userName + "\"}"));
            for (GameDecision decision : decisions) {
                jsonArray.put(decision.toJson());
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, this.url, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
              Toast.makeText(mContext, "Successfully Sent Results", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Error Sending the Results", Toast.LENGTH_SHORT).show();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.mRequestQueue.add(request);
    }
}
