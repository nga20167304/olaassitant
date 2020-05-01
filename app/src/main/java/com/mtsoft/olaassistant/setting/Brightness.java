package com.mtsoft.olaassistant.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.mtsoft.olaassistant.R;

/**
 * Created by manhhung on 4/2/19.
 */

public class Brightness {
    public static void initBrightness(int brightness, Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(context.getApplicationContext())) {
                    // Do stuff here
                    Settings.System.putInt(context.getContentResolver(),
                            Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

                    brightness = Settings.System.getInt(context.getContentResolver(),
                            Settings.System.SCREEN_BRIGHTNESS);
                    Log.e("brightness", "" + brightness);
                }
                else {
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }


        } catch (Settings.SettingNotFoundException e) {
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }
    }

    public static void initSeekbarBrightness(int brightness, final Context context, final Activity activity) {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(context);
        final SeekBar seek = new SeekBar(context);
        seek.setProgress(brightness);
        seek.setMax(255);
        popDialog.setIcon(R.drawable.ic_brightness);
        popDialog.setTitle("Độ sáng màn hình");
        popDialog.setView(seek);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateBrightness(progress, context, activity);
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

    public static void updateBrightness( int newBrightness, Context context, Activity activity) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, newBrightness);
        WindowManager.LayoutParams layoutpars = activity.getWindow().getAttributes();
        layoutpars.screenBrightness = newBrightness / (float) 255;
        activity.getWindow().setAttributes(layoutpars);
    }

}
