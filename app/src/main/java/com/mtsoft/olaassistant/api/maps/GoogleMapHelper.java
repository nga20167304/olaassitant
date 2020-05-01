package com.mtsoft.olaassistant.api.maps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by manhhung on 4/2/19.
 */

public class GoogleMapHelper {

    public static void openGoogleMaps(ArrayList<String> locations, Context context) {
        Log.e("locations", " " + locations);
        if (locations.size() > 0) {
            String uri = "http://maps.google.com/maps?saddr=&daddr=" + locations.get(0).trim().replaceAll(" ", "+");
            if (locations.size() >= 2) {
                uri = "http://maps.google.com/maps?saddr=" + locations.get(0).trim().replaceAll(" ", "+")
                        + "&daddr=" + locations.get(1).trim().replaceAll(" ", "+");
            }
            Log.e("URI_MAP", uri);
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(uri)
            );
            intent.setPackage("com.google.android.apps.maps");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }
}
