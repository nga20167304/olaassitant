package com.mtsoft.olaassistant.myinterface;

import com.mtsoft.olaassistant.models.Location;
import com.mtsoft.olaassistant.models.Weather;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by manhhung on 3/31/19.
 */

public interface TaskCompleted {
    public void onTaskComplete(Map<String, ArrayList<Weather>> dictionaryWeather);
    public void onTaskComplete(ArrayList<Location> locations, String type);

}
