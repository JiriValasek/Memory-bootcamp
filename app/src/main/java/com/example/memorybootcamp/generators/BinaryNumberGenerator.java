package com.example.memorybootcamp.generators;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BinaryNumberGenerator implements IGenerator{

    @Override
    public JSONObject generateSequence(int length) {
        JSONObject result = new JSONObject();
        JSONArray sequence = new JSONArray();
        int elem;
        for (int i = 0; i < length; i++) {
            elem = (int) Math.round(Math.random());
            sequence.put(String.valueOf(elem));
        }
        try {
            result.put("questions", sequence);
            result.put("responses", sequence);
        }
        catch (JSONException e){
            return null;
        }
        return result;
    }
}
