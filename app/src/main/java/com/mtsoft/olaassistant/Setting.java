package com.mtsoft.olaassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by manhhung on 3/8/19.
 */

public class Setting {
    public Context context;

    public Setting(Context context) {
        this.context = context;
    }

    public void enabledWifi(boolean isEnabled) {
        Log.e("Wifi", "" + isEnabled);
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(isEnabled);
    }

    public void enabledBluetooth(boolean isEnabled) {
        Log.e("BLUETOOTH", "" + isEnabled);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Log.e("BLUETOOTH", "Device does not support Bluetooth");
            Toast.makeText(context, "Device does not support Bluetooth", Toast.LENGTH_LONG).show();
        }
//
//        if (mBluetoothAdapter.isEnabled()) {
//            mBluetoothAdapter.disable();
//        }

        if (isEnabled) {
            mBluetoothAdapter.enable();
        } else {
            mBluetoothAdapter.disable();
        }
    }

    public void enabledSilentMode(boolean isEnabled, Context mContext) {
        Log.e("SilentMode", "" + isEnabled);
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                && !notificationManager.isNotificationPolicyAccessGranted()) {
//
//            Intent intent = new Intent(
//                    android.provider.Settings
//                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
////            mContext.startActivity(intent);
//            am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//            return;
//        }

        if (isEnabled) {
            am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    public void enabledNormalMode(boolean isEnabled) {
        Log.e("NormalMode", "" + isEnabled);
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (isEnabled) {
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } else {
            am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }

    public void enabledVibrateMode(boolean isEnabled) {
        Log.e("enabledVibrateMode", "" + isEnabled);
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (isEnabled) {
            am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);//rung
        } else {
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

        }
    }

    public void enabledFlightMode(boolean isEnabled) {
        Log.e("FlightMode", "" + isEnabled);

        if (android.os.Build.VERSION.SDK_INT < 17) {
            try {
                // toggle airplane mode
                Settings.System.putInt(
                        context.getContentResolver(),
                        Settings.System.AIRPLANE_MODE_ON, isEnabled ? 1 : 0);

                // Post an intent to reload
                Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
                intent.putExtra("state", isEnabled);
                context.sendBroadcast(intent);
            } catch (ActivityNotFoundException e) {
                Log.e("FLIGHT MODE", e.getMessage());
            }
        } else {
            try {
                Intent intent = new Intent(android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                try {
                    Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Log.e("FLIGHT MODE", "Error: " + ex.toString());
                }
            }
        }

    }

    public void enabledLocation(boolean isEnabled) {
        Log.e("Location", "" + isEnabled);
        if (!hasGPSDevice(context)) {
            Toast.makeText(context, "Gps not Supported", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    public boolean isCameraUsebyApp() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (RuntimeException e) {
            return true;
        } finally {
            if (camera != null) camera.release();
        }
        return false;
    }

    public void enabledFlash(boolean enabled, Camera camera) {
        isCameraUsebyApp();

        PackageManager pm = context.getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.e("err", "Device has no camera!");
            return;
        }
        if (enabled) {
            final Camera.Parameters p = camera.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(p);
            camera.startPreview();
        } else {
            try {
                isCameraUsebyApp();
                final Camera.Parameters p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                camera.stopPreview();
            }catch (RuntimeException e) {
                Log.e("FLASH", e.getMessage());
            }

        }

    }

    public boolean checkCamera(Activity mActivity) {
        Boolean isFlashAvailable = mActivity.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {

            AlertDialog alert = new AlertDialog.Builder(mActivity)
                    .create();
            alert.setTitle("Error !!");
            alert.setMessage("Your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                }
            });
            alert.show();
            return false;
        }

        return true;
    }

    public void turnOffScreen() {
        Log.e("OffScreen", "------");

    }


    public void setMobileDataEnabled(boolean enabled) {
        try {
            Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            try {
                Intent intent = new Intent("android.settings.ACTION_DATA_ROAMING_SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Log.e("DataEnabled MODE", "Error: " + ex.toString());
            }
        }
    }

}

