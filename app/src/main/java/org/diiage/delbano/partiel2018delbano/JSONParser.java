package org.diiage.delbano.partiel2018delbano;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bastien on 16/05/2018.
 */

public class JSONParser {
    public Release JSONToRelease(JSONObject json) throws JSONException{

        Release release = new Release(json.getString("status"), json.getString("thumb"), json.getString("format"),
                json.getString("title"), json.getString("catno"), json.optInt("year"),
                json.getString("resource_url"), json.getString("artist"), json.getInt("id"));
        return release;
    }
}
