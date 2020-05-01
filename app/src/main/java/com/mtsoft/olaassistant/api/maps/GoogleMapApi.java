package com.mtsoft.olaassistant.api.maps;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mtsoft.olaassistant.models.Location;
import com.mtsoft.olaassistant.myinterface.TaskCompleted;
import com.mtsoft.olaassistant.utils.ReadJsonData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by manhhung on 3/31/19.
 */

public class GoogleMapApi extends AsyncTask<ArrayList<String>, Void, ArrayList<String>>  {
    private Context mContext;

    ProgressDialog mProgress;
    private TaskCompleted mCallback;
    private String type;

    public GoogleMapApi(Context context, String type){
        this.mContext = context;
        this.mCallback = (TaskCompleted) context;
        this.type = type;

    }

    @Override
    public void onPreExecute() {
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Please wait...");
        mProgress.show();
    }


    @Override
    protected ArrayList<String> doInBackground(ArrayList<String> ... params) {
        ArrayList<String> results = new ArrayList<>();
        for (String location : params[0]) {
            Log.e("link: ", location);
            results.add(ReadJsonData.readUrl(location));
        }
        Log.e("AAA", "len " + results.size());
        return results;
    }

    @Override
    protected void onPostExecute(ArrayList<String> results) {
        super.onPostExecute(results);
        mProgress.dismiss();

        Log.e("len results", results.size() + "");

        ArrayList<Location> locations = new ArrayList<>();

        for (String result : results) {
            if (result != null) {
                JSONObject json = null;
                try {
                    json = new JSONObject(result);
                    String status = json.getString("status");
                    if (status.equalsIgnoreCase("ok")) {
                        JSONArray jsonArray = json.getJSONArray("results");
                        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                        if (jsonObject.has("formatted_address")) {
                            String address = jsonObject.getString("formatted_address");
                            JSONObject geometry = jsonObject.getJSONObject("geometry");
                            JSONObject locatoion = geometry.getJSONObject("location");
                            String lat = locatoion.getString("lat");
                            String lng = locatoion.getString("lng");
                            locations.add(new Location(lat, lng, address));
                            Log.e("Location", "lat: " + lat + " lng " + lng + " address: " + address);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    mProgress.dismiss();
                }
            }
        }

        Log.e("Len location: ",  locations.size() + "");

        //This is where you return data back to caller
        mCallback.onTaskComplete(locations, type);
    }
}