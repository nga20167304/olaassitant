package com.mtsoft.olaassistant.api.youtube;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.mtsoft.olaassistant.myinterface.YoutubeApiCompleted;
import com.mtsoft.olaassistant.utils.ReadJsonData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by manhhung on 4/4/19.
 */

public class YoutubeApi extends AsyncTask<String, String, String> {

    private Context mContext;
    ProgressDialog mProgress;
    private YoutubeApiCompleted mCallback;

    public YoutubeApi(Context context) {
        this.mContext = context;
        this.mCallback = (YoutubeApiCompleted) context;

    }

    @Override
    public void onPreExecute() {
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Đang lấy thông tin video...");
        mProgress.show();
    }


    protected String doInBackground(String... params) {
        return ReadJsonData.readUrl(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        String _id = null;
        if (result != null) {
            JSONObject json = null;
            try {
                json = new JSONObject(result);
                JSONArray jsonArray = json.getJSONArray("items");
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                if (jsonObject.has("id")) {
                    JSONObject idObj = (JSONObject) jsonObject.get("id");
                    String videoId = idObj.getString("videoId");
                    _id = videoId;
                }

            } catch (JSONException e) {
                mCallback.onTaskCompleteVideo(_id);
                mProgress.dismiss();
                e.printStackTrace();
            }
        }
        mCallback.onTaskCompleteVideo(_id);
        mProgress.dismiss();
    }
}

