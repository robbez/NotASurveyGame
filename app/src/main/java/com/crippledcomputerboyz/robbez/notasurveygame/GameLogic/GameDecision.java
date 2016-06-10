package com.crippledcomputerboyz.robbez.notasurveygame.GameLogic;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by robbez on 5/23/16.
 */
public class GameDecision {
    private int sceneIndex;
    private String data;

    public GameDecision(int sceneIndex, String data) {
        this.sceneIndex = sceneIndex;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getSceneIndex() {
        return sceneIndex;
    }

    public void setSceneIndex(int sceneIndex) {
        this.sceneIndex = sceneIndex;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("sceneIndex", this.sceneIndex);
        jo.put("data", this.data);
        return jo;
    }
}
