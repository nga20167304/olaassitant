package com.mtsoft.olaassistant.api.weather;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mtsoft.olaassistant.models.Weather;
import com.mtsoft.olaassistant.models.WeatherDaily;
import com.mtsoft.olaassistant.models.WeatherHourly;
import com.mtsoft.olaassistant.myinterface.TaskCompleted;
import com.mtsoft.olaassistant.utils.ReadJsonData;
import com.mtsoft.olaassistant.utils.WeatherUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by manhhung on 3/31/19.
 */

public class WeatherApi extends AsyncTask<String, Void, String> {
    private Context mContext;

    ProgressDialog mProgress;
    private TaskCompleted mCallback;

    public WeatherApi(Context context) {
        this.mContext = context;
        this.mCallback = (TaskCompleted) context;

    }

    @Override
    public void onPreExecute() {
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Please wait...");
        mProgress.show();
    }


    @Override
    protected String doInBackground(String... params) {
        return ReadJsonData.readUrl(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mProgress.dismiss();
        Map<String, ArrayList<Weather>> dictionaryWeather = new HashMap<String, ArrayList<Weather>>();
//        dictionaryWeather.put("dog", "type of animal");

        ArrayList<Weather> currently = new ArrayList<>();
        ArrayList<Weather> hourly = new ArrayList<>();
        ArrayList<Weather> daily = new ArrayList<>();


        if (result != null) {
            JSONObject json = null;
            try {
                json = new JSONObject(result);
                if (json.has("currently")) {
                    JSONObject currentlyObj = json.getJSONObject("currently");
                    Weather wCurrently = parseJsonWeatherHourly(currentlyObj);
                    currently.add(wCurrently);
                }

                if (json.has("hourly")) {
                    JSONObject hourlyObj = json.getJSONObject("hourly");
                    JSONArray dataHourly = hourlyObj.getJSONArray("data");
                    for (int i = 0; i < dataHourly.length(); i++) {
                        Weather hWeather = parseJsonWeatherHourly((JSONObject) dataHourly.get(i));
                        hourly.add(hWeather);
                    }
                }

                if (json.has("daily")) {
                    JSONObject dailyObj = json.getJSONObject("daily");
                    JSONArray datadaily = dailyObj.getJSONArray("data");
                    for (int i = 0; i < datadaily.length(); i++) {
                        Weather dWeather = parseJsonWeatherDaily((JSONObject) datadaily.get(i));
                        daily.add(dWeather);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                mProgress.dismiss();
            }
        }

        dictionaryWeather.put("currently", currently);
        Log.e("currently", dictionaryWeather.get("currently") + "");
        dictionaryWeather.put("hourly", hourly);
        Log.e("hourly", hourly.size() + "");
        dictionaryWeather.put("daily", daily);
        Log.e("daily", dictionaryWeather.get("daily") + "");

        Log.e("DIC", "" + dictionaryWeather);


        //This is where you return data back to caller
        mCallback.onTaskComplete(dictionaryWeather);
    }

    public WeatherHourly parseJsonWeatherHourly(JSONObject object) {
        WeatherHourly weather = null;
        try {
            weather = new WeatherHourly(
                    object.getLong("time"),
                    object.getString("summary"),
                    object.getString("icon"),
                    object.getString("windSpeed"),
                    object.getDouble("humidity")*100,
                    WeatherUtil.convertF2C(object.getDouble("temperature"))
            );

        } catch (JSONException e) {

            return null;
        }

        return weather;
    }

    public WeatherDaily parseJsonWeatherDaily(JSONObject object) {
        WeatherDaily weather = null;
        try {
            weather = new WeatherDaily(
                    object.getLong("time"),
                    object.getString("summary"),
                    object.getString("icon"),
                    object.getString("windSpeed"),
                    object.getDouble("humidity") * 100,
                    WeatherUtil.convertF2C(object.getDouble("temperatureHigh")),
                    object.getLong("temperatureHighTime"),
                    WeatherUtil.convertF2C(object.getDouble("temperatureLow")),
                    object.getLong("temperatureLowTime"),
                    WeatherUtil.convertF2C(object.getDouble("temperatureMin")),
                    object.getLong("temperatureMinTime"),
                    WeatherUtil.convertF2C(object.getDouble("temperatureMax")),
                    object.getLong("temperatureMaxTime")
            );

        } catch (JSONException e) {

            return null;
        }

        return weather;
    }

}
