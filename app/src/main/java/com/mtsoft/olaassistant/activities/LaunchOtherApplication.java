package com.mtsoft.olaassistant.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.mtsoft.olaassistant.R;
import com.mtsoft.olaassistant.Text2Speech;
import com.mtsoft.olaassistant.api.maps.GoogleMapApi;
import com.mtsoft.olaassistant.api.maps.GpsTracker;
import com.mtsoft.olaassistant.models.Location;
import com.mtsoft.olaassistant.models.Song;
import com.mtsoft.olaassistant.models.Weather;
import com.mtsoft.olaassistant.myinterface.TaskCompleted;
import com.mtsoft.olaassistant.utils.AppUtils;
import com.mtsoft.olaassistant.utils.ReadEvents;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class LaunchOtherApplication extends AppCompatActivity implements TaskCompleted {
    private TextView txvResult;
    private EditText edtYoutube, edtZing, edtTTS;
    private Button btnSearchZing, btnSearchYoutube, btnTTS;
    FloatingActionButton fab;
    public static final int REQUEST_CODE_SPEECH2TXT = 10;

    public static final String ZING_MP3_PKG = "com.zing.mp3";
    private static final String ZING_URL = "https://zingmp3.vn";

    public static Song song = null;

    TextToSpeech tts;
    Text2Speech text2Speech;


    private GpsTracker gpsTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        setContentView(R.layout.activity_launch_other_application);

        txvResult = (TextView) this.findViewById(R.id.txvResult);

        edtZing = (EditText) this.findViewById(R.id.edtZing);
        edtYoutube = (EditText) this.findViewById(R.id.edtYoutube);
        edtYoutube.setText("Minh Khai Hai Bà Trưng Hà Nội");
        edtTTS = (EditText) this.findViewById(R.id.edtTTS);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.getDefault());
            }
        });

        text2Speech = new Text2Speech(tts);

        btnTTS = (Button) this.findViewById(R.id.btnTTS);
        btnTTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtTTS = edtTTS.getText().toString();
                if (txtTTS.trim().length() > 0) {
                    text2Speech.speech(txtTTS);
                }

            }
        });


        btnSearchYoutube = (Button) this.findViewById(R.id.btnSearchYoutube);
        btnSearchYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = edtYoutube.getText().toString();
                query = query.trim();
                if (query.length() <= 0) {
                    return;
                }
                text2Speech.speech(query);
//                new JsonTaskYoutube().execute(AppUtils.setUrlQueryYoutube(query));
                ArrayList<String> params = new ArrayList<>();
                params.add(AppUtils.setUrlGetLatLng(query));
                params.add(AppUtils.setUrlGetLatLng("Keangnam Mễ Trì Từ Liêm Hà Nội"));
                new GoogleMapApi(LaunchOtherApplication.this, "location").execute(params);

            }
        });
        btnSearchZing = (Button) this.findViewById(R.id.btnSearchZing);
        btnSearchZing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                launchGoogleSpeech2Text();
//                getLocation();
//                Log.e("TIME", AppUtils.getTimeMillis());
//                new WeatherApi(LaunchOtherApplication.this).execute("https://api.darksky.net/forecast/17fdbe391ebf3056d60ae06d4620093f/21.0055546,105.8434628?lang=en");

//                openTimePickerDialog(true);

                ReadEvents readCalendar = new ReadEvents(LaunchOtherApplication.this, getApplicationContext());
                readCalendar.getEvents(edtYoutube.getText().toString());
            }
        });

    }





    public void watchYoutubeVideo(String id) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppUtils.WEBVIEW_YOUTUBE + id));
        webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent appIntent = new Intent(Intent.ACTION_VIEW);
        appIntent.setPackage("com.google.android.youtube");
        appIntent.putExtra("id", id);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            getApplicationContext().startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            getApplicationContext().startActivity(webIntent);
        }

    }

    /*
    * TODO: Launch Google speech to text
    **/
    public void launchGoogleSpeech2Text() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }


    public void setupReminder(Long time, String location, String event) {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", time);
        intent.putExtra("allDay", false);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);
        intent.putExtra("rrule", "FREQ=DAILY");
//        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", event);
        startActivity(intent);
    }


    public void getLocation() {
        gpsTracker = new GpsTracker(LaunchOtherApplication.this);
        if (gpsTracker.canGetLocation()) {
            txvResult.setText(gpsTracker.getCurrentLocation().toString());
        } else {
            gpsTracker.showSettingsAlert();
        }
    }


    public void startMap(ArrayList<Location> locations) {

        if (locations.size() > 0) {
            String link = "http://maps.google.com/maps?saddr=&daddr=" + locations.get(1).getLat() + "," + locations.get(1).getLng();
            if (locations.size() >= 2) {
                link = "http://maps.google.com/maps?saddr=" +
                        locations.get(0).getLat() + "," + locations.get(0).getLng() +
                        "&daddr=" + locations.get(1).getLat() + "," + locations.get(1).getLng();
            }
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(link)
            );
            intent.setPackage("com.google.android.apps.maps");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getApplicationContext().startActivity(intent);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_SPEECH2TXT:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txvResult.setText(result.get(0));
                }
                break;
        }
    }


    @Override
    public void onTaskComplete(ArrayList<Location> locations, String type) {
        if (type.equalsIgnoreCase("location")) {
            startMap(locations);
        }
    }

    @Override
    public void onTaskComplete(Map<String, ArrayList<Weather>> dictionaryWeather) {
        ArrayList<Weather> weatherDaily = dictionaryWeather.get("daily");
        ArrayList<Weather> weatherHourly = dictionaryWeather.get("hourly");
        ArrayList<Weather> weatherCurrently = dictionaryWeather.get("currently");

        for (Weather w : weatherDaily) {
            Log.e("W", w.toString());
        }

    }


}
