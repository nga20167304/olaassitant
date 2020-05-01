package com.mtsoft.olaassistant.api.music;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.mtsoft.olaassistant.models.Song;
import com.mtsoft.olaassistant.myinterface.Mp3ZingApiCompleted;
import com.mtsoft.olaassistant.utils.ReadJsonData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by manhhung on 4/3/19.
 */

public class Mp3ZingApi extends AsyncTask<String, String, String> {
    private Context mContext;
    ProgressDialog mProgress;
    private Mp3ZingApiCompleted mCallback;

    public Mp3ZingApi(Context context) {
        this.mContext = context;
        this.mCallback = (Mp3ZingApiCompleted) context;

    }

    @Override
    public void onPreExecute() {
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Đang lấy thông tin bài hát...");
        mProgress.show();
    }

    protected String doInBackground(String... params) {
        return ReadJsonData.readUrl(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Song song = null;
        if (result != null) {
            JSONObject json = null;
            try {
                json = new JSONObject(result);
                JSONArray jsonArray = json.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    if (jsonObject.has("song")) {
                        JSONArray songArray = (JSONArray) jsonObject.get("song");
                        if (songArray.length() == 0) {
                            mProgress.dismiss();
                            mCallback.onTaskCompleteMusic(song);
                            return;
                        }
                        JSONObject songObj = (JSONObject) songArray.get(0);
                        String artist = songObj.getString("artist");
                        String name = songObj.getString("name");
                        String id = songObj.getString("id");
                        String type = "song";
                        song = new Song(artist, name, id, type);
                        Log.e("SONG", " " + song.toString());
                    }
                }
            } catch (JSONException e) {
                mCallback.onTaskCompleteMusic(song);
                mProgress.dismiss();
                e.printStackTrace();
            }
        }
        mProgress.dismiss();
        mCallback.onTaskCompleteMusic(song);

    }
}
