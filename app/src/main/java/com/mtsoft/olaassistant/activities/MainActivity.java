package com.mtsoft.olaassistant.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.mtsoft.olaassistant.Call;
import com.mtsoft.olaassistant.R;
import com.mtsoft.olaassistant.Setting;
import com.mtsoft.olaassistant.Text2Speech;
import com.mtsoft.olaassistant.adapter.ContactAdapter;
import com.mtsoft.olaassistant.adapter.EventAdapter;
import com.mtsoft.olaassistant.adapter.MessageAdapter;
import com.mtsoft.olaassistant.api.maps.GoogleMapApi;
import com.mtsoft.olaassistant.api.maps.GpsTracker;
import com.mtsoft.olaassistant.api.music.Mp3ZingApi;
import com.mtsoft.olaassistant.api.weather.WeatherApi;
import com.mtsoft.olaassistant.api.youtube.YoutubeApi;
import com.mtsoft.olaassistant.matcher.LunarMatcher;
import com.mtsoft.olaassistant.matcher.MusicMatcher;
import com.mtsoft.olaassistant.matcher.VideoMatcher;
import com.mtsoft.olaassistant.models.Chat;
import com.mtsoft.olaassistant.models.Contact;
import com.mtsoft.olaassistant.models.Event;
import com.mtsoft.olaassistant.models.Location;
import com.mtsoft.olaassistant.models.Song;
import com.mtsoft.olaassistant.models.Weather;
import com.mtsoft.olaassistant.models.WeatherDaily;
import com.mtsoft.olaassistant.models.WeatherHourly;
import com.mtsoft.olaassistant.myinterface.Mp3ZingApiCompleted;
import com.mtsoft.olaassistant.myinterface.TaskCompleted;
import com.mtsoft.olaassistant.myinterface.YoutubeApiCompleted;
import com.mtsoft.olaassistant.service.FloatingWidgetService;
import com.mtsoft.olaassistant.setting.CameraHelper;
import com.mtsoft.olaassistant.utils.AppUtils;
import com.mtsoft.olaassistant.utils.CalendarUtils;
import com.mtsoft.olaassistant.utils.ReadEvents;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.ApplicationsName;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, TaskCompleted, Mp3ZingApiCompleted, YoutubeApiCompleted {
    public static final String TAG = "MainActivity";

    public static final int REQUEST_CODE_SPEECH2TXT = 100;
    public static final int REQUEST_CODE_WRITE_SETTING = 101;
    public static final int CALL_REQUEST = 103;
    private static final int CAMERA_REQUEST_CODE = 104;

    public static String phoneNumber = "";

    public boolean isFindHour = false;
//    @BindView(R.id.status)
//    TextView status;
//    @BindView(R.id.textMessage)
//    TextView textMessage;
//    private SpeechAPI speechAPI;
//    private VoiceRecorder mVoiceRecorder;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private CameraManager mCameraManager;
    private String mCameraId;
    private Boolean isTorchOn;

    private ArrayList<Chat> chats;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerView;

    private GpsTracker gpsTracker;

    private int brightness;

    private TextToSpeech tts;
    private Text2Speech text2Speech;

    private Long timestamp;

    private CameraHelper cameraHelper;


    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
//    private Merlin merlin;


    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        speechAPI = new SpeechAPI(MainActivity.this);

        initViews();


        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.getDefault());
            }
        });
        text2Speech = new Text2Speech(tts);

        gpsTracker = new GpsTracker(MainActivity.this);


        isTorchOn = false;
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        //
        timestamp = CalendarUtils.getTimestampCurrent();


        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


    }

    /**
     * Set and initialize the view elements.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initializeView() {
        if (!Settings.canDrawOverlays(getApplicationContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {
                //If the draw over permission is not available open the settings screen
                //to grant the permission.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            }
        } else {
            startService(new Intent(MainActivity.this, FloatingWidgetService.class));
            finish();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (isGrantedPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
//            startVoiceRecorder();
//        } else {
//            makeRequest(Manifest.permission.RECORD_AUDIO);
//        }
//        if (speechAPI == null) {
//            speechAPI = new SpeechAPI(MainActivity.this);
//        }
//        speechAPI.addListener(mSpeechServiceListener);
//        JumpingBeans.with(status)
//                .appendJumpingDots()
//                .build();
    }

    @Override
    protected void onResume() {
        /*if (speechAPI == null) {
            speechAPI = new SpeechAPI(MainActivity.this);
        }*/
//        merlin.bind();

        gpsTracker = new GpsTracker(MainActivity.this);
//        if (camera == null) {
//            camera = Camera.open();
//        }
        super.onResume();
    }

    @Override
    protected void onPause() {
//        merlin.unbind();

        super.onPause();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStop() {
//        stopVoiceRecorder();

        // Stop Cloud Speech API
       /* speechAPI.removeListener(mSpeechServiceListener);
        speechAPI.destroy();
        speechAPI = null;*/

//        stopCamera();
        if (isTorchOn && cameraHelper != null) {
            cameraHelper.turnOffFlashLight(mCameraManager, mCameraId);
        }

//        if (Settings.canDrawOverlays(this)) {
//            initializeView();
//        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        stopCamera();
        if (isTorchOn && cameraHelper != null) {
            cameraHelper.turnOffFlashLight(mCameraManager, mCameraId);
        }
        gpsTracker.stopUsingGPS();
        super.onDestroy();
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_chat);
        chats = new ArrayList<>();
        chats.add(new Chat("Welcome to Ola Assistant!", "ola"));
        messageAdapter = new MessageAdapter(this, chats);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        mLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(messageAdapter);
        fab.setOnClickListener(this);

//        zoomIn.setOnClickListener(this);
    }

    private void addDrawerItems() {
        final String[] osArray = {"Hướng dẫn", "Thu nhỏ", "Rate me", "Đóng góp", "...."};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        Intent intentWelcome = new Intent(getApplicationContext(), WelcomeActivity.class);
                        intentWelcome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intentWelcome);
                        Toast.makeText(getApplicationContext(), osArray[position], Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        initializeView();
                        Toast.makeText(getApplicationContext(), osArray[position], Toast.LENGTH_LONG).show();
                        break;

                    case 2:
                        Toast.makeText(getApplicationContext(), osArray[position], Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), osArray[position], Toast.LENGTH_LONG).show();
                        break;
                }

            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            initializeView();
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public void stopCamera() {
//        if (camera != null) {
//            camera.stopPreview();
//            camera.release();
//            camera = null;
//            Log.e("CAMERA", "start activity cam");
//        }
//    }

    public void showContact(ArrayList<Contact> contactArrayList) {
        // TODO Auto-generated method stub

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        // create view for add item in dialog
        View contactView = (View) inflater.inflate(R.layout.listview_contact, null);
        // on dialog cancel button listner
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });

        // add custom view in dialog
        alertDialog.setView(contactView);

        RecyclerView recyclerViewContact = contactView.findViewById(R.id.recycler_view_contact);
        final AlertDialog alert = alertDialog.create();
        alert.setTitle(" Select contact...."); // Title
        ContactAdapter contactAdapter = new ContactAdapter(MainActivity.this, contactArrayList, alert, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(this, 1);
        mLayoutManager1.setAutoMeasureEnabled(true);
        recyclerViewContact.setLayoutManager(mLayoutManager1);
        recyclerViewContact.setItemAnimator(new DefaultItemAnimator());
        recyclerViewContact.setAdapter(contactAdapter);
        alert.show();
    }

    public void showEvents(ArrayList<Event> eventArrayList) {
        // TODO Auto-generated method stub

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        // create view for add item in dialog
        View eventView = (View) inflater.inflate(R.layout.listview_event, null);
        // on dialog cancel button listner
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

        // add custom view in dialog
        alertDialog.setView(eventView);

        RecyclerView recyclerViewContact = eventView.findViewById(R.id.recycler_view_event);
        final AlertDialog alert = alertDialog.create();
        alert.setTitle("Danh sách nhắc nhở...."); // Title
        EventAdapter eventAdapter = new EventAdapter(MainActivity.this, eventArrayList, alert, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(this, 1);
        mLayoutManager1.setAutoMeasureEnabled(true);
        recyclerViewContact.setLayoutManager(mLayoutManager1);
        recyclerViewContact.setItemAnimator(new DefaultItemAnimator());
        recyclerViewContact.setAdapter(eventAdapter);
        alert.show();
        alert.setCancelable(false);
    }


    public void settingBrightness() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(getApplicationContext())) {
                    // Do stuff here
                    Settings.System.putInt(getContentResolver(),
                            Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    brightness = Settings.System.getInt(getContentResolver(),
                            Settings.System.SCREEN_BRIGHTNESS);
                    Log.e("brightness", "" + brightness);
                    initSeekbarBrightness();
                    text2Speech.speech("Bắt đầu thiết lập độ sáng màn hình");
                } else {
                    showAlertPermissionScreen();

                }
            } else {
                Settings.System.putInt(getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

                brightness = Settings.System.getInt(getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS);
                Log.e("brightness", "" + brightness);
                initSeekbarBrightness();
                text2Speech.speech("Bắt đầu thiết lập độ sáng màn hình");
            }


        } catch (Settings.SettingNotFoundException e) {
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }
    }

    public void initSeekbarBrightness() {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final SeekBar seek = new SeekBar(this);
        seek.setProgress(brightness);
        seek.setMax(255);
        popDialog.setIcon(R.drawable.ic_brightness);
        popDialog.setTitle("Screen brightness");
        popDialog.setView(seek);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateBrightness(progress);
            }

            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }
        });

        // Button OK
        popDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        popDialog.create();
        popDialog.show();

    }

    public void updateBrightness(int newBrightness) {
        brightness = newBrightness;
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
        WindowManager.LayoutParams layoutpars = getWindow().getAttributes();
        layoutpars.screenBrightness = brightness / (float) 255;
        getWindow().setAttributes(layoutpars);
    }

    private void initSound() {

        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final Dialog popDialog = new Dialog(this, R.style.theme_sms_receive_dialog);
        popDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                (int) (WindowManager.LayoutParams.MATCH_PARENT * 0.9));
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.sounds_dialog, (ViewGroup) findViewById(R.id.your_dialog_root_element), false);
        popDialog.setContentView(layout);

        popDialog.setTitle("Sounds and notifications");
        TextView btnDismiss = (TextView) layout.findViewById(R.id.your_dialog_button);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popDialog.dismiss();
            }
        });
        SeekBar ringtone = (SeekBar) layout.findViewById(R.id.ringtone_seekbar);
        SeekBar media = (SeekBar) layout.findViewById(R.id.media_seekbar);
        SeekBar notifications = (SeekBar) layout.findViewById(R.id.notifications_seekbar);
        SeekBar system = (SeekBar) layout.findViewById(R.id.system_seekbar);

        try {

            media.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            media.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));
            media.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });


            ringtone.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_RING));
            ringtone.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_RING));
            ringtone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_RING,
                            progress, 0);
                }
            });

            notifications.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
            notifications.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_NOTIFICATION));
            notifications.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
                            progress, 0);
                }
            });

            system.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
            system.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_SYSTEM));
            system.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,
                            progress, 0);
                }
            });

            popDialog.create();
            popDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replyOfAssistant(String answer) {
        text2Speech.speech(answer);
        chats.add(chats.size(), new Chat(answer, "ola"));
        messageAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(chats.size() - 1);
    }

    public void openGoogleMaps(ArrayList<String> locations) {
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
            getApplicationContext().startActivity(intent);
        }
    }

    public void playMp3(Song song) {
        String name = song.getName().trim().replaceAll(" ", "-");
        String artist = song.getArtist().trim().replaceAll(" ", "-");
        String _id = song.getId().trim();
        PackageManager pm = getApplicationContext().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(AppUtils.URL_PLAYER_ZING + name + artist + "/" + _id + ".html"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (AppUtils.isPackageInstalled(ApplicationsName.PAKAGENAME_ZINGMP3, pm)) {
            intent.setPackage(ApplicationsName.PAKAGENAME_ZINGMP3);
            getApplicationContext().startActivity(intent);
        } else {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppUtils.URL_PLAYER_ZING + name + artist + "/" + _id + ".html"));
            webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(webIntent);
        }
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

    public void setupReminder(Long time, String location, String event) {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", time * 1000);
        intent.putExtra("allDay", false);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);
//        intent.putExtra("rrule", "FREQ=DAILY");
//        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", event);
        startActivity(intent);
    }

    public void launchGoogleSpeech2Text() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SPEECH2TXT);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    public void showAlertPermissionScreen() {
        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialog.setTitle("Cài đặt");
        // Setting Dialog Message
        alertDialog.setMessage("Để điều chỉnh độ sáng màn hình. Vui lòng cấp quyền thiết lập!");
        // On pressing Settings button
        alertDialog.setPositiveButton("Cài đặt", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplication().getPackageName()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, REQUEST_CODE_WRITE_SETTING);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void openWebsite(String url) {
        String urlString = url;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null);
            startActivity(intent);
        }
    }

    public void setAlarm(String sentence) {
        if (sentence == null || sentence.length() < 1) {
            return;
        }
        ArrayList<Integer> alarmDays = CalendarUtils.findDayOfWeeks(sentence);
        Integer h = CalendarUtils.getHourMinuteAlarm(sentence).get("h");
        Integer m = CalendarUtils.getHourMinuteAlarm(sentence).get("m");

        Log.e("Days: ", "" + alarmDays);
        Log.e("H", "" + h);
        Log.e("M", "" + m);

        Intent openClockIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
        openClockIntent
                .putExtra(AlarmClock.EXTRA_DAYS, alarmDays)
                .putExtra(AlarmClock.EXTRA_HOUR, h)
                .putExtra(AlarmClock.EXTRA_MINUTES, m);


        openClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(openClockIntent);
    }

//    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {
//
//        @Override
//        public void onVoiceStart() {
//            if (speechAPI != null) {
//                speechAPI.startRecognizing(mVoiceRecorder.getSampleRate());
//            }
//        }
//
//        @Override
//        public void onVoice(byte[] data, int size) {
//            if (speechAPI != null) {
//                speechAPI.recognize(data, size);
//            }
//        }
//
//        @Override
//        public void onVoiceEnd() {
//            if (speechAPI != null) {
//                speechAPI.finishRecognizing();
//            }
//        }
//
//    };
//    private final SpeechAPI.Listener mSpeechServiceListener =
//            new SpeechAPI.Listener() {
//                @Override
//                public void onSpeechRecognized(final String text, final boolean isFinal) {
//                    if (isFinal) {
//                        mVoiceRecorder.dismiss();
//                    }
//                    if (textMessage != null && !TextUtils.isEmpty(text)) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (isFinal) {
//                                    textMessage.setText(null);
//                                    chats.add(chats.size(), new Chat(text, "user"));
//                                    new PostData().execute(text);
//                                    messageAdapter.notifyDataSetChanged();
////                                    messageAdapter.notifyItemInserted(0);
//                                } else {
//                                    textMessage.setText(text);
//                                }
//                            }
//                        });
//                    }
//                }
//            };
//
//    private int isGrantedPermission(String permission) {
//        return ContextCompat.checkSelfPermission(this, permission);
//    }
//
//    private void makeRequest(String permission) {
//        ActivityCompat.requestPermissions(this, new String[]{permission}, RECORD_REQUEST_CODE);
//    }
//
//    private void startVoiceRecorder() {
//        if (mVoiceRecorder != null) {
//            mVoiceRecorder.stop();
//        }
//        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
//        mVoiceRecorder.start();
//    }
//
//    private void stopVoiceRecorder() {
//        if (mVoiceRecorder != null) {
//            mVoiceRecorder.stop();
//            mVoiceRecorder = null;
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == RECORD_REQUEST_CODE) {
//            if (grantResults.length == 0 && grantResults[0] == PackageManager.PERMISSION_DENIED
//                    && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                finish();
//            } else {
//                startVoiceRecorder();
//            }
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Log.e(TAG, "click fab");
                launchGoogleSpeech2Text();
                break;
//            case R.id.notify_me:
//                if (!Settings.canDrawOverlays(this)) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
//                        //If the draw over permission is not available open the settings screen
//                        //to grant the permission.
//                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                                Uri.parse("package:" + getPackageName()));
//                        startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
//                    }
//                } else {
//                    initializeView();
//                }
//                break;
        }
    }

    /*
    * TODO: Launch Google speech to text
    **/


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_SPEECH2TXT:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String sentence = result.get(0);
                    chats.add(chats.size(), new Chat(sentence, "user"));
                    messageAdapter.notifyDataSetChanged();
                    float scoreMusic = MusicMatcher.getScoreMusic(sentence);
                    float scoreVideo = VideoMatcher.getScoreVideo(sentence);

                    if (scoreVideo > scoreMusic && scoreVideo > 1.799) {
                        String video = VideoMatcher.getVideo(sentence);
                        Log.e(TAG, "" + video);
                        Log.e(TAG, AppUtils.setUrlQueryYoutube(video));
                        replyOfAssistant("Video đang được phát");
                        new YoutubeApi(MainActivity.this).execute(AppUtils.setUrlQueryYoutube(video));
                        return;
                    }

                    if (scoreMusic > scoreVideo && scoreMusic > 1.799) {
                        String song = MusicMatcher.getSong(sentence);
                        if (song != null && song.length() > 0) {
                            song = song.replaceAll("của ", "");
                            Log.e(TAG, "song: " + song);
                            song = song.replaceAll(" ", "%20");
                            new Mp3ZingApi(MainActivity.this).execute(AppUtils.URL_ZING + song);
                            replyOfAssistant("Bài hát đang được phát");
                            return;
                        }
                    }

                    if (LunarMatcher.isQuestionLunar(sentence)) {
                        replyOfAssistant(CalendarUtils.anwserLunar(sentence));
                        Log.e("Am lich", "am lich");
                        return;
                    }else {
                        Log.e("s", sentence);
                        Log.e("K", "Lỗi");
                    }
                    new PostData().execute(sentence);
                }
                break;
            case REQUEST_CODE_WRITE_SETTING:
                Log.e(TAG, "setting bright...");
                settingBrightness();
                break;
            case 103:
                Log.e(TAG, "CALL");

            case CODE_DRAW_OVER_OTHER_APP_PERMISSION:
                //Check if the permission is granted or not.
                if (resultCode == RESULT_OK) {
                    initializeView();
                } else { //Permission is not available
                    Toast.makeText(this,
                            "Draw over other app permission not available. Closing the application",
                            Toast.LENGTH_SHORT).show();

//                    finish();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == CALL_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Call call = new Call(this, this);
                call.callNumber(MainActivity.phoneNumber);
                Toast.makeText(this, "Calling " + MainActivity.phoneNumber, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
                replyOfAssistant("Đã bật Camera");
            }
        }
    }

    String anwserlocationWeather = "";

    @Override
    public void onTaskComplete(ArrayList<Location> locations, String type) {
        if (type.equalsIgnoreCase("location")) {
            Log.e("Find Locations: ", locations.size() + "");
            text2Speech.speech("Thưa sếp! Đã định vị được vị trí.");
        }
        if (type.equalsIgnoreCase("weather")) {
            if (locations.size() > 0) {
                Location locationWeather = locations.get(0);
//                replyOfAssistant(locationWeather.getName());
                anwserlocationWeather = locationWeather.getName().replaceAll("Unnamed Road,", "");
                String latlng = locationWeather.getLat() + "," + locationWeather.getLng();
                Log.e(TAG, AppUtils.setUrlGetWeather(latlng, timestamp, "en"));
                new WeatherApi(MainActivity.this).execute(AppUtils.setUrlGetWeather(latlng, timestamp, "en"));
            }
        }
    }

    @Override
    public void onTaskComplete(Map<String, ArrayList<Weather>> dictionaryWeather) {
        ArrayList<Weather> weatherDaily = dictionaryWeather.get("daily");
        ArrayList<Weather> weatherHourly = dictionaryWeather.get("hourly");
        ArrayList<Weather> weatherCurrently = dictionaryWeather.get("currently");
        if (weatherCurrently.size() == 0 || weatherHourly.size() == 0 || weatherCurrently.size() == 0) {
            replyOfAssistant("Tải dữ liệu thời tiết không thành công");
            return;
        }

        if (!isFindHour) {
            WeatherDaily daily = (WeatherDaily) weatherDaily.get(0);
            Long timeCurrently = daily.getTime();
            String summary = daily.getSummary();
            String icon = daily.getIcon();
            String windSpeed = daily.getWindSpeed();
            Double humidity = daily.getHumidity();
            Double temperatureHigh = daily.getTemperatureHigh();
            Double temperatureLow = daily.getTemperatureLow();
            Long temperatureHighTime = daily.getTemperatureHighTime();
            Long temperatureLowTime = daily.getTemperatureLowTime();

            String time = CalendarUtils.convertMilliseconds2DateFormat(timeCurrently * 1000);

            String replyWeather = "Thời tiết: " + time.split(" ")[1] + " \n" +
                    "Tại: " + anwserlocationWeather + " \n" +
                    "Summary: " + summary + " \n" +
                    "Nhiệt độ cao nhất: " + temperatureHigh + " \n" +
                    "Nhiệt độ thấp nhất: " + temperatureLow + " \n" +
                    "Tốc độ gió: " + windSpeed + "m/s" + " \n" +
                    "Độ ẩm: " + (humidity) + "%";
            replyOfAssistant(replyWeather);
        } else {
            Weather currently = weatherCurrently.get(0);
            Long timeCurrently = currently.getTime();
            String summary = currently.getSummary();
            String icon = currently.getIcon();
            String windSpeed = currently.getWindSpeed();
            Double humidity = currently.getHumidity();

            Double t = 0d;

            for (int i = 0; i < weatherHourly.size(); i++) {
                WeatherHourly hourly = (WeatherHourly) weatherHourly.get(i);
                if (Math.abs(timeCurrently - hourly.getTime()) < 3600) {
                    t = hourly.getTemperature();
                    break;
                }
            }

            String time = CalendarUtils.convertMilliseconds2DateFormat(timeCurrently * 1000);
            String replyWeather = "Thời tiết: " + time + " \n" +
                    "Tại: " + anwserlocationWeather + " \n" +
                    "Summary: " + summary + " \n" +
                    "Nhiệt độ: " + t + " \n" +
                    "Tốc độ gió: " + windSpeed + "m/s" + " \n" +
                    "Độ ẩm: " + (humidity) + "%";
            replyOfAssistant(replyWeather);
        }


        for (Weather w : weatherDaily) {
            Log.e("W", w.toString());
        }
    }

    @Override
    public void onTaskCompleteMusic(Song song) {
        if (song != null) {
            playMp3(song);
        }
    }

    @Override
    public void onTaskCompleteVideo(String _id) {
        watchYoutubeVideo(_id);
    }

    private class PostData extends AsyncTask<String, Void, String> {
        ProgressDialog mProgress;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress = new ProgressDialog(MainActivity.this);
            mProgress.setCancelable(false);
            mProgress.setMessage("Đang tải dữ liệu...");
            mProgress.setButton(-1, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    mProgress.dismiss();
                    for (okhttp3.Call call : okHttpClient.dispatcher().queuedCalls()) {
                        if (call.request().tag().equals("ola")) {
                            call.cancel();
                            Log.e(TAG, "CANCEL");
                        }
                    }
                    for (okhttp3.Call call : okHttpClient.dispatcher().runningCalls()) {
                        if (call.request().tag().equals("ola")) {
                            call.cancel();
                            Log.e(TAG, "CANCEL");
                        }
                    }
                }
            });
            mProgress.show();


        }

        @Override
        protected String doInBackground(String... strings) {
            String text = strings[0];
            Log.d("SENTENCE", text);
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("sentence", text)
                    .setType(MultipartBody.FORM)
                    .build();

            Request request = new Request.Builder()
                    .url(AppUtils.URL_SERVER)
                    .post(requestBody)
                    .tag("ola")
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
//                e.printStackTrace();
                Log.e(TAG, "Đã xảy ra lỗi trong khi tải dữ liệu");
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String response) {
            mProgress.dismiss();
            Log.d("RESPONSE", "" + response);
            if (response == null) {
                replyOfAssistant("Quá trình tải thất bại");
                return;
            }
            try {
                JSONObject responseJSON = new JSONObject(response);
                String domain = responseJSON.getString("domain");
                Log.d("DOMAIN", domain);
                String content = responseJSON.getString("content");
                Log.d("CONTENT", content);

                switch (domain) {
                    case ApplicationsName.WELCOME:
                        String[] replyWelcome = {
                                "Xin chào! Tôi có thể giúp gì được ngài?",
                                "Chào ông chủ! Tôi có thể giúp gì?",
                                "Xin chào! Bạn muốn giúp gì?"
                        };
                        Random rand = new Random();

                        int n = rand.nextInt(replyWelcome.length);
                        if (text2Speech != null) {
                            replyOfAssistant(replyWelcome[n]);
                        }
                        break;
                    case ApplicationsName.APPLICATION:
                        String action = responseJSON.getString("action");
                        Setting setting = new Setting(getBaseContext());
                        switch (content) {
                            case ApplicationsName.WIFI:
                                if (action.equals("action_off")) {
//                                    setting.enabledWifi(false);
                                    replyOfAssistant("Vui lòng bật Wifi để sử dụng trợ lý ảo!");
                                } else {
                                    setting.enabledWifi(true);
                                    replyOfAssistant("Wifi đang được bật");
                                }
                                return;
                            case ApplicationsName.BLUETOOTH:
                                if (action.equals("action_off")) {
                                    setting.enabledBluetooth(false);
                                    replyOfAssistant("Bluetooth đã tắt");
                                } else {
                                    setting.enabledBluetooth(true);
                                    replyOfAssistant("Bluetooth đã kích hoạt");
                                }
                                return;
                            case ApplicationsName.LOCATION_SETTING:
                                if (action.equals("action_off")) {
                                    setting.enabledLocation(false);

                                } else {
                                    if (gpsTracker.canGetLocation()) {
                                        replyOfAssistant("Vị trí đã bật rồi thưa ngài!");
                                        return;
                                    } else {
                                        setting.enabledLocation(true);
                                    }
                                }
                                replyOfAssistant("Điều hướng tới cài đặt vị trí");
                                return;
                            case ApplicationsName.FLIGHT_MODE:
                                if (action.equals("action_off")) {
                                    setting.enabledFlightMode(false);
                                } else {
                                    setting.enabledFlightMode(true);
                                }
                                replyOfAssistant("Điều hướng tới thiết lập chế độ máy bay");
                                return;
                            case ApplicationsName.SCREEN_OFF:
                                if (action.equals("action_off")) {
                                    setting.turnOffScreen();
                                }
                                return;
                            case ApplicationsName.SILENT_MODE:
                                if (action.equals("action_off")) {
                                    setting.enabledSilentMode(false, MainActivity.this);
                                    replyOfAssistant("Đã tắt chế độ im lặng");
                                } else {
                                    setting.enabledSilentMode(true, MainActivity.this);
                                    replyOfAssistant("Đã bật chế độ im lặng");
                                }
                                return;
                            case ApplicationsName.Vibrate_MODE:
                                if (action.equals("action_off")) {
                                    setting.enabledVibrateMode(false);
                                    replyOfAssistant("Đã tắt chế độ rung");
                                } else {
                                    setting.enabledVibrateMode(true);
                                    replyOfAssistant("Đã bật chế độ rung");
                                }
                                return;
                            case ApplicationsName.NORMAL_MODE:
                                if (action.equals("action_off")) {
                                    setting.enabledNormalMode(false);
                                    replyOfAssistant("Đã tắt âm thanh");

                                } else {
                                    setting.enabledNormalMode(true);
                                    replyOfAssistant("Đã bật chế độ âm thanh");

                                }
                                return;
                            case ApplicationsName.FLASH:
                                if (action.equals("action_off")) {

                                    if (cameraHelper != null) {
                                        cameraHelper.turnOffFlashLight(mCameraManager, mCameraId);
                                        isTorchOn = false;
                                        replyOfAssistant("Đèn pin đã tắt");
                                    }
                                } else {
                                    Camera camera = null;
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                        camera = Camera.open();
                                    } else {
                                        cameraHelper = new CameraHelper(camera, MainActivity.this);
                                        cameraHelper.turnOnFlashLight(mCameraManager, mCameraId);
                                        isTorchOn = true;
                                    }

                                    replyOfAssistant("Đèn pin đã được kích hoạt");

                                }
                                return;
                            case ApplicationsName.SETTING_SOUNDS:
                                replyOfAssistant("Xin mời thiết lập âm thanh");
                                initSound();
                                return;
                            case ApplicationsName.INTERNET_3G:
                                if (action.equals("action_off")) {
                                    setting.setMobileDataEnabled(false);
                                    replyOfAssistant("Đã tắt 3G");

                                } else {
                                    setting.setMobileDataEnabled(true);
                                    replyOfAssistant("Đã bật 3G");

                                }
                                return;
                            case ApplicationsName.CAMERA_VIDEO:
//                                stopCamera();
                                Log.e("VIDEO CAPTURE", "Video");
//                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                                Intent chooserIntent = Intent.createChooser(takePictureIntent, "Capture Image or Video");
//                                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takeVideoIntent});
//                                startActivity(chooserIntent);

                                if (checkSelfPermission(Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED) {

                                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                                            CAMERA_REQUEST_CODE);
                                } else {
                                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                    startActivity(intent);
                                    replyOfAssistant("Đã bật Camera");
                                }
                                return;
                            case ApplicationsName.IMAGE_VIDEO:
                                replyOfAssistant("Đã bật kho hình ảnh và video");
                                Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
                                mediaChooser.setType("video/*, image/*");
                                startActivity(mediaChooser);
                                return;
                            case ApplicationsName.INBOX_MESSAGE:
                                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                sendIntent.setData(Uri.parse("sms:"));
                                startActivity(sendIntent);
                                return;
                            case ApplicationsName.CONTACT:
                                Log.e(TAG, "contact...");
                                Intent intentContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                startActivity(intentContact);
                                return;
                            case ApplicationsName.SETTING_APPLICATION:
                                Log.e("SETTING APP", "setting application");
                                startActivityForResult(
                                        new Intent(android.provider.Settings.ACTION_SETTINGS),
                                        0);
                                replyOfAssistant("Cài đặt điện thoại");
                                return;
                            case ApplicationsName.CALENDAR:
                                Intent i = new Intent();
                                ComponentName cn = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");
                                cn = new ComponentName("com.android.calendar", "com.android.calendar.LaunchActivity");
                                i.setComponent(cn);
                                startActivity(i);
                                replyOfAssistant(CalendarUtils.getCurrentTimeUsingCalendar());
                                return;
                        }
                        lauchApplication(ApplicationsName.getApplication(content), content);
                        break;
                    case ApplicationsName.CALL:
                        String type = responseJSON.getString("type");
                        // Check the SDK version and whether the permission is already granted or not.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 102);
                            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                        } else {
                            Call call = new Call(getApplicationContext(), MainActivity.this);
                            if (type.equalsIgnoreCase("number")) {
                                ArrayList<Contact> contactsNumber = new ArrayList<>();
                                contactsNumber.add(new Contact("", content));
                                showContact(contactsNumber);
                            } else if (type.equalsIgnoreCase("name")) {
                                String ret = call.getPhoneNumber(content);
                                Log.e("CALL", "RET : " + ret);

                                call.getCandidate(content);
                                showContact(call.getCandidate(content));
                            } else {
                                Log.e("CALL", "Unable to identify who to call");
                                content = "Unable to identify who to call";
                            }

                            chats.add(chats.size(), new Chat(content, "ola"));
                        }
                        break;
                    case ApplicationsName.SETTING:
                        if (content.equals("screen")) {
                            settingBrightness();
                        } else if (content.equals("sound")) {
                            initSound();
                            text2Speech.speech("Bắt đầu thiết lập âm lượng");
                        } else {
                            chats.add(chats.size(), new Chat(domain + " unknow!", "ola"));

                        }

                        break;
                    case ApplicationsName.ALARM:
                        setAlarm(content);
                        replyOfAssistant("Báo thức đã được thiết lập.");
                        break;
                    case ApplicationsName.LOCATION_DOMAIN:

                        if (responseJSON.has("location")) {
                            JSONObject locationObj = responseJSON.getJSONObject("location");
                            Log.e("locationObj", "" + locationObj);
                            ArrayList<String> locations = new ArrayList<>();

                            String location1 = null;
                            String location2 = null;
                            if (locationObj.has("location_1")) {
                                location1 = locationObj.getString("location_1");
                                locations.add(location1);
                            }
                            if (locationObj.has("location_2")) {
                                location2 = locationObj.getString("location_2");
                                locations.add(location2);

                            }
                            if (location2 != null && location1 != null) {
                                replyOfAssistant("Đang tìm đường từ " + location1 + " đi đến " + location2);
                            }
                            if (location1 != null && location2 == null) {
                                replyOfAssistant("Đang tìm đường đi đến " + location1 + " thư sếp!");
                            }
                            if (locations.size() == 0) {
                                replyOfAssistant("Thưa sếp không thể định vị được");
                                return;
                            }
                            Log.e("locations", locations.toString());
                            openGoogleMaps(locations);
//                            GoogleMapHelper.openGoogleMaps(locations, MainActivity.this);
                        }

//                        chats.add(chats.size(), new Chat(content, "ola"));


                        break;
                    case ApplicationsName.WEATHER:
                        String location = null;
                        String time = null;
                        if (responseJSON.has("weather")) {
                            JSONObject weather = responseJSON.getJSONObject("weather");
                            if (weather.has("location")) {
                                location = weather.getString("location");
                            }
                            if (weather.has("time")) {
                                time = weather.getString("time");
                            }
                        }


                        if (time == null || time.trim().length() < 1) {
                            Log.e(TAG, "TIME not detect");
                            timestamp = CalendarUtils.getTimestampCurrent() / 1000 + CalendarUtils.getNextNumber(content) / 1000;
                        } else {
                            Log.e(TAG, "TIME detect");
                            timestamp = CalendarUtils.getTimestamp(time) + CalendarUtils.getNextNumber(content) / 1000;
                            if (CalendarUtils.findHour(time) != null) {
                                isFindHour = true;
                            }
                        }

                        if (location == null || location.trim().length() < 1) {
                            if (gpsTracker.canGetLocation()) {
                                Location currentLocation = gpsTracker.getCurrentLocation();
                                String latlng = currentLocation.getLat() + "," + currentLocation.getLng();
                                Log.e(TAG, AppUtils.setUrlGetWeather(latlng, timestamp, "en"));
                                ArrayList<String> params = new ArrayList<>();
                                params.add(AppUtils.setUrlGetLatLng(latlng));
                                new GoogleMapApi(MainActivity.this, "weather").execute(params);
                            } else {
                                gpsTracker.showSettingsAlert();
                            }

                        } else {
                            ArrayList<String> params = new ArrayList<>();
                            params.add(AppUtils.setUrlGetLatLng(location));
                            new GoogleMapApi(MainActivity.this, "weather").execute(params);
                        }
                        break;
                    case ApplicationsName.EVENT:
                        if (responseJSON.has("event")) {
                            String address = "";
                            String contentEvent = "";
                            String timeEvent = "";
                            Long timestamp = CalendarUtils.getTimestampCurrent();
                            JSONObject eventObj = responseJSON.getJSONObject("event");
                            if (eventObj.has("address")) {
                                address = eventObj.getString("address");
                            }
                            if (eventObj.has("time")) {
                                timeEvent = eventObj.getString("time");
                                timestamp = CalendarUtils.getTimestamp(content);
                            }
                            if (eventObj.has("content")) {
                                contentEvent = eventObj.getString("content");
                            }

                            setupReminder(timestamp, address, contentEvent);
                        }

                        break;

                    case ApplicationsName.SITE:
                        if (responseJSON.has("site")) {
                            JSONObject siteObj = responseJSON.getJSONObject("site");
                            if (siteObj.has("site")) {
                                String site = siteObj.getString("site");
                                if (site.trim().length() > 0) {
                                    if (site.contains(".")) {
                                        openWebsite("http://" + site);
                                    } else {
                                        Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
                                        search.putExtra(SearchManager.QUERY, site);
                                        startActivity(search);
                                    }
                                }
                            }
                        }
                        break;
                    case ApplicationsName.QUESTION_EVENT:
                        if (responseJSON.has("event")) {
                            ReadEvents readEvents = new ReadEvents(MainActivity.this, getApplicationContext());
                            ArrayList<Event> events = readEvents.getEvents(content);
                            if (events.size() > 0) {
                                Log.e(TAG, events.toString());
                                showEvents(events);
                                replyOfAssistant("Ngài có " + events.size() + " sự kiện");
                                if (events.size() == 1) {
                                    Event event = events.get(0);
                                    replyOfAssistant("Sự kiện: " + event.getTitle() +
                                            "\nThời gian: " + event.getTime() +
                                            "\n" + "Địa điểm: " + event.getLocation());
                                }
                            } else {
                                replyOfAssistant("Thưa ngài không tìm thấy sự kiện nào!");
                            }
                        }
                        break;

                    default:
                        replyOfAssistant("Làm ơn nói lại");
                }

                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(chats.size() - 1);
                super.onPostExecute(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        public void lauchApplication(String packageName, String content) {
            Intent application = getPackageManager().getLaunchIntentForPackage(packageName);
            if (application != null) {
                startActivity(application);
                replyOfAssistant("Đang mở ứng dụng " + content);
            } else {
                messageAdapter.notifyDataSetChanged();
                replyOfAssistant("Không tìm thấy ứng dụng " + content);
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                }
            }
        }
    }
}
